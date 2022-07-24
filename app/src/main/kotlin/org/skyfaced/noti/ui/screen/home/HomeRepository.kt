package org.skyfaced.noti.ui.screen.home

import kotlinx.coroutines.flow.Flow
import org.skyfaced.noti.ui.model.note.Note
import org.skyfaced.noti.util.Result

interface HomeRepository {
    val notesFlow: Flow<List<Note>>

    suspend fun deleteNote(note: Note): Result<Unit>

    suspend fun insertNote(note: Note): Result<Unit>
}