package org.skyfaced.noti.ui.screen.details

import org.skyfaced.noti.database.dao.NoteDao
import org.skyfaced.noti.ui.model.note.Note
import org.skyfaced.noti.ui.model.note.NoteEntityMapper
import org.skyfaced.noti.ui.model.note.NoteMapper
import org.skyfaced.noti.util.Result
import org.skyfaced.noti.util.exception.NoteNotFound
import org.skyfaced.noti.util.exception.NoteNotInserted
import org.skyfaced.noti.util.result

class DetailsRepositoryImpl(
    private val noteDao: NoteDao,
    private val noteMapper: NoteMapper = NoteMapper(),
    private val noteEntityMapper: NoteEntityMapper = NoteEntityMapper(),
) : DetailsRepository {
    override suspend fun fetchNoteById(id: Long): Result<Note> = result {
        val entity = noteDao.fetchNoteById(id) ?: return@result Result.Failure(NoteNotFound())
        return@result Result.Success(noteMapper.map(entity))
    }

    override suspend fun upsertNote(note: Note): Result<Unit> = result {
        val entity = noteEntityMapper.map(note)
        val rowId = noteDao.insert(entity)
        if (rowId < 0) return@result Result.Failure(NoteNotInserted())
        else return@result Result.Success(Unit)
    }
}