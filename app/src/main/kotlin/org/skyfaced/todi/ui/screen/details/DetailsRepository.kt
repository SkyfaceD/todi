package org.skyfaced.todi.ui.screen.details

import org.skyfaced.todi.ui.model.note.Note
import org.skyfaced.todi.util.Result

interface DetailsRepository {
    suspend fun fetchNoteById(id: Long): Result<Note>

    suspend fun upsertNote(id: Long, title: String, description: String): Result<Unit>
}