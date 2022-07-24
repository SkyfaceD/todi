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
import org.skyfaced.todi.util.exception.FlowException
import org.skyfaced.todi.util.onFailure
import org.skyfaced.todi.util.onSuccess
import org.skyfaced.todi.util.withData

class HomeViewModel(
    private val settings: TodiSettings,
    private val homeRepository: HomeRepository,
    private val uiMessageManager: UiMessageManager = UiMessageManager()
) : ViewModel() {
    var state by mutableStateOf(HomeUiState())
        private set

    private var notesJob: Job? = null

    init {
        viewModelScope.launch {
            applySettings()
            subscribeOnMessages()
            fetchNotes()
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch { uiMessageManager.clearMessage(id) }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            homeRepository.deleteNote(note)
                .onSuccess {
                    uiMessageManager.emitMessage(UiMessage(R.string.msg_note_deleted).withData(note))
                }
                .onFailure { uiMessageManager.emitMessage(UiMessage(it)) }
        }
    }

    fun refresh() {
        state = state.copy(notes = null)
        notesJob?.cancel()
        fetchNotes()
    }

    fun undo(note: Note) {
        viewModelScope.launch {
            homeRepository.insertNote(note)
                .onFailure { uiMessageManager.emitMessage(UiMessage(it)) }
        }
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
                    uiMessageManager.emitMessage(UiMessage(FlowException()))
                    state = state.copy(isLoading = false)
                }
                .collect { state = state.copy(isLoading = false, notes = it) }
        }
        notesJob?.start()
    }

    private fun subscribeOnMessages() {
        viewModelScope.launch {
            uiMessageManager.message.collect { state = state.copy(uiMessage = it) }
        }
    }
}

data class HomeUiState(
    val gridCells: Int = 1,
    val descriptionMaxLines: Int = 3,
    val notes: List<Note>? = null,
    val isLoading: Boolean = true,
    val uiMessage: UiMessage? = null,
)