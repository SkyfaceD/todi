package org.skyfaced.todi.ui.screen.details

import org.skyfaced.todi.database.dao.NoteDao
import org.skyfaced.todi.database.entity.NoteEntity
import org.skyfaced.todi.ui.model.note.Note
import org.skyfaced.todi.ui.model.note.NoteMapper
import org.skyfaced.todi.util.Result
import org.skyfaced.todi.util.exception.NoteNotFound
import org.skyfaced.todi.util.exception.NoteNotInserted
import org.skyfaced.todi.util.result

class DetailsRepositoryImpl(
    private val noteDao: NoteDao,
    private val noteMapper: NoteMapper = NoteMapper(),
) : DetailsRepository {
    override suspend fun fetchNoteById(id: Long): Result<Note> = result {
        val entity = noteDao.fetchNoteById(id) ?: return@result Result.Failure(NoteNotFound())
        return@result Result.Success(noteMapper.map(entity))
    }

    override suspend fun upsertNote(
        id: Long,
        title: String,
        description: String,
    ): Result<Unit> = result {
        val entity = NoteEntity(id, title, description)
        val rowId = noteDao.insert(entity)
        if (rowId < 0) return@result Result.Failure(NoteNotInserted())
        else return@result Result.Success(Unit)
    }
}