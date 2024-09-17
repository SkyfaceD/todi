package org.skyfaced.noti.ui.screen.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.skyfaced.noti.database.dao.NoteDao
import org.skyfaced.noti.settings.NotiSettings
import org.skyfaced.noti.ui.model.note.Note
import org.skyfaced.noti.ui.model.note.NoteEntityMapper
import org.skyfaced.noti.ui.model.note.NoteMapper
import org.skyfaced.noti.util.Result
import org.skyfaced.noti.util.exception.NoteNotDeleted
import org.skyfaced.noti.util.exception.NoteNotInserted
import org.skyfaced.noti.util.result

class HomeRepositoryImpl(
    private val noteDao: NoteDao,
    private val settings: NotiSettings,
    private val noteMapper: NoteMapper = NoteMapper(),
    private val noteEntityMapper: NoteEntityMapper = NoteEntityMapper(),
) : HomeRepository {
    override val notesFlow: Flow<List<Note>> =
        noteDao.fetchNotesFlow().map { it.map(noteMapper::map) }

    override suspend fun deleteNote(note: Note): Result<Unit> = result(settings.locale) {
        val entity = noteEntityMapper.map(note)
        val rowCount = noteDao.delete(entity)
        return@result if (rowCount == 1) success(Unit)
        else failure(NoteNotDeleted(settings.locale))
    }

    override suspend fun insertNote(note: Note): Result<Unit> = result(settings.locale) {
        val entity = noteEntityMapper.map(note)
        val rowId = noteDao.insert(entity)
        return if (rowId != -1L) success(Unit)
        else failure(NoteNotInserted(settings.locale))
    }
}