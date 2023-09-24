package org.skyfaced.noti.ui.screen.home

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
import org.skyfaced.noti.settings.NotiSettings
import org.skyfaced.noti.settings.strings
import org.skyfaced.noti.ui.model.note.Note
import org.skyfaced.noti.util.UiMessage
import org.skyfaced.noti.util.UiMessageManager
import org.skyfaced.noti.util.exception.FlowException
import org.skyfaced.noti.util.onFailure
import org.skyfaced.noti.util.onSuccess
import org.skyfaced.noti.util.withData

class HomeViewModel(
    private val settings: NotiSettings,
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
                    val uiMessage = UiMessage(settings.locale.strings.msg_note_deleted)
                    uiMessageManager.emitMessage(uiMessage.withData(note))
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
            ) { (gridCells) ->
                state = state.copy(
                    gridCells = gridCells,
                )
            }.collect { }
        }
    }

    private fun fetchNotes() {
        notesJob = viewModelScope.launch {
            homeRepository.notesFlow
                .onStart { state = state.copy(isLoading = true) }
                .catch {
                    uiMessageManager.emitMessage(UiMessage(FlowException(settings.locale)))
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