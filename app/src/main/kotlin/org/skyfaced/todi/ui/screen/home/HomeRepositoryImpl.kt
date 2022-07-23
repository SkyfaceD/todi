package org.skyfaced.todi.ui.screen.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.skyfaced.todi.database.dao.NoteDao
import org.skyfaced.todi.ui.model.note.Note
import org.skyfaced.todi.ui.model.note.NoteEntityMapper
import org.skyfaced.todi.ui.model.note.NoteMapper
import org.skyfaced.todi.util.Result
import org.skyfaced.todi.util.exception.NoteNotDeleted
import org.skyfaced.todi.util.exception.NoteNotInserted
import org.skyfaced.todi.util.result

class HomeRepositoryImpl(
    private val noteDao: NoteDao,
    private val noteMapper: NoteMapper = NoteMapper(),
    private val noteEntityMapper: NoteEntityMapper = NoteEntityMapper(),
) : HomeRepository {
    override val notesFlow: Flow<List<Note>> =
        noteDao.fetchNotesFlow().map { it.map(noteMapper::map) }

    override suspend fun deleteNote(note: Note): Result<Unit> = result {
        val entity = noteEntityMapper.map(note)
        val rowCount = noteDao.delete(entity)
        return@result if (rowCount == 1) success(Unit)
        else failure(NoteNotDeleted())
    }

    override suspend fun insertNote(note: Note): Result<Unit> = result {
        val entity = noteEntityMapper.map(note)
        val rowId = noteDao.insert(entity)
        return if (rowId != -1L) success(Unit)
        else failure(NoteNotInserted())
    }
}