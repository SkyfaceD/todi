package org.skyfaced.todi.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.skyfaced.todi.ui.model.task.TaskShort

class HomeViewModel : ViewModel() {
    var state by mutableStateOf(HomeUiState())
        private set
}

data class HomeUiState(
    val list: List<TaskShort> = emptyList()
)