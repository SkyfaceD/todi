package org.skyfaced.todi.ui.screen.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.skyfaced.todi.ui.model.note.Note
import org.skyfaced.todi.util.UiMessage
import org.skyfaced.todi.util.UiMessageManager
import org.skyfaced.todi.util.onFailure
import org.skyfaced.todi.util.onSuccess

class DetailsViewModel(
    private val detailsRepository: DetailsRepository,
    savedStateHandle: SavedStateHandle,
    private val uiMessageManager: UiMessageManager = UiMessageManager(),
) : ViewModel() {
    val mode: Mode = Mode.valueOf(savedStateHandle.get<String>("mode").orEmpty())
    val id: Long = savedStateHandle.get<Long>("id") ?: 0L

    var state by mutableStateOf(DetailsUiState())
        private set

    init {
        viewModelScope.launch {
            subscribeOnMessages()
            if (id != 0L) fetchNote() else createNote()
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch { uiMessageManager.clearMessage(id) }
    }

    fun updateTitle(title: String) {
        state = state.copy(note = state.note?.copy(title = title))
    }

    fun updateDescription(description: String) {
        state = state.copy(note = state.note?.copy(description = description))
    }

    fun upsert() {
        viewModelScope.launch {
            state = state.copy(isUpserting = true)
            detailsRepository.upsertNote(state.note!!)
                .onSuccess { state = state.copy(isUpserting = false, upserted = true) }
                .onFailure {
                    uiMessageManager.emitMessage(UiMessage(it))
                    state = state.copy(isUpserting = false)
                }
        }
    }

    private fun createNote() {
        state = state.copy(note = Note(0, "", ""))
    }

    private fun fetchNote() {
        viewModelScope.launch {
            detailsRepository.fetchNoteById(id)
                .onSuccess { state = state.copy(note = it) }
                .onFailure { uiMessageManager.emitMessage(UiMessage(it)) }
        }
    }

    private fun subscribeOnMessages() {
        viewModelScope.launch {
            uiMessageManager.message.collect { state = state.copy(uiMessage = it) }
        }
    }
}

data class DetailsUiState(
    val note: Note? = null,
    val uiMessage: UiMessage? = null,
    val isUpserting: Boolean = false,
    val upserted: Boolean = false,
)