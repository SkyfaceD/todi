package org.skyfaced.noti.ui.screen.details

import org.skyfaced.noti.ui.model.note.Note
import org.skyfaced.noti.util.Result

interface DetailsRepository {
    suspend fun fetchNoteById(id: Long): Result<Note>

    suspend fun upsertNote(note: Note): Result<Unit>
}