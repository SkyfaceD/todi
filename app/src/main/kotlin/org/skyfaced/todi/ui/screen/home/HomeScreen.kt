package org.skyfaced.todi.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    HomeScreen(
        state = viewModel.state,
    )
}

@Composable
private fun HomeScreen(
    state: HomeUiState,
) {
}