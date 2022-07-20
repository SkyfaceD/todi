package org.skyfaced.todi.ui.screen.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class DetailsViewModel(
    private val mode: Mode,
    private val id: Long = -1,
    private val detailsRepository: DetailsRepository
) : ViewModel() {
    var state by mutableStateOf(DetailsUiState())
        private set

    fun updateTitle(title: String) {
        state = state.copy(title = title)
    }

    fun updateDescription(description: String) {
        state = state.copy(description = description)
    }
}

data class DetailsUiState(
    val title: String = "",
    val description: String = "",
)