package org.skyfaced.todi.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.skyfaced.todi.ui.screen.details.Mode
import org.skyfaced.todi.ui.screen.navigation.Screens
import org.skyfaced.todi.ui.util.LocalTodiNavigation
import java.util.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val navHostController = LocalTodiNavigation.current

    Box(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            navHostController.navigate(
                Screens.Details.argRoute(
                    UUID.randomUUID(),
                    Mode.View
                )
            )
        }) {
            Text("Go to details")
        }
    }

//    HomeScreen(
//        state = viewModel.state,
//    )
}

@Composable
private fun HomeScreen(
    state: HomeUiState,
) {
}