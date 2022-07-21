package org.skyfaced.todi.ui.screen.home

import kotlinx.coroutines.flow.Flow
import org.skyfaced.todi.ui.model.note.Note
import org.skyfaced.todi.util.Result

interface HomeRepository {
    val notesFlow: Flow<List<Note>>

    suspend fun deleteNote(note: Note): Result<Unit>
}