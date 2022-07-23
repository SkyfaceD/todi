package org.skyfaced.todi.ui.screen.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
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
        if (id != 0L) fetchNote()
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch { uiMessageManager.clearMessage(id) }
    }

    fun updateTitle(title: String) {
        state = state.copy(title = title)
    }

    fun updateDescription(description: String) {
        state = state.copy(description = description)
    }

    fun upsert() {
        viewModelScope.launch {
            state = state.copy(isUpserting = true)
            detailsRepository.upsertNote(id, state.title, state.description)
                .onSuccess { state = state.copy(isUpserting = false, upserted = true) }
                .onFailure { state = state.copy(isUpserting = false, uiMessage = UiMessage(it)) }
        }
    }

    private fun fetchNote() {
        viewModelScope.launch {
            detailsRepository.fetchNoteById(id)
                .onSuccess { state = state.copy(title = it.title, description = it.description) }
                .onFailure { state = state.copy(uiMessage = UiMessage(it)) }
        }
    }
}

data class DetailsUiState(
    val title: String = "",
    val description: String = "",
    val uiMessage: UiMessage? = null,
    val isUpserting: Boolean = false,
    val upserted: Boolean = false,
)