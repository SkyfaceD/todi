package org.skyfaced.todi.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.skyfaced.todi.R
import org.skyfaced.todi.settings.TodiSettings
import org.skyfaced.todi.ui.model.note.Note
import org.skyfaced.todi.util.UiMessage
import org.skyfaced.todi.util.UiMessageManager
import org.skyfaced.todi.util.consume
import org.skyfaced.todi.util.exception.FlowException

class HomeViewModel(
    private val settings: TodiSettings,
    private val homeRepository: HomeRepository,
    private val uiMessageManager: UiMessageManager = UiMessageManager(),
) : ViewModel() {
    var state by mutableStateOf(HomeUiState())
        private set

    private var notesJob: Job? = null

    init {
        viewModelScope.launch {
            applySettings()
            fetchNotes()
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch { uiMessageManager.clearMessage(id) }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            homeRepository.deleteNote(note).consume(
                onSuccess = {
                    state = state.copy(uiMessage = UiMessage(R.string.msg_note_deleted))
                },
                onFailure = { state = state.copy(uiMessage = UiMessage(it)) }
            )
        }
    }

    fun refresh() {
        state = state.copy(notes = null)
        notesJob?.cancel()
        fetchNotes()
    }

    private fun applySettings() {
        viewModelScope.launch {
            combine(
                settings.gridCells.observe,
                settings.descriptionMaxLines.observe
            ) { gridCells, descriptionMaxLines ->
                state = state.copy(
                    gridCells = gridCells,
                    descriptionMaxLines = descriptionMaxLines
                )
            }.collect { }
        }
    }

    private fun fetchNotes() {
        notesJob = viewModelScope.launch {
            homeRepository.notesFlow
                .onStart { state = state.copy(isLoading = true) }
                .catch {
                    state = state.copy(isLoading = false, uiMessage = UiMessage(FlowException()))
                }
                .collect { state = state.copy(isLoading = false, notes = it) }
        }
        notesJob?.start()
    }
}

data class HomeUiState(
    val gridCells: Int = 1,
    val descriptionMaxLines: Int = 2,
    val notes: List<Note>? = null,
    val isLoading: Boolean = true,
    val uiMessage: UiMessage? = null,
)