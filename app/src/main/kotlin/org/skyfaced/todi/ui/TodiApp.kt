package org.skyfaced.todi.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.skyfaced.todi.ui.theme.TodiTheme
import org.skyfaced.todi.ui.util.LocalTodiNavigation

@Composable
fun TodiApp() {
    val navHostController = rememberNavController()

    CompositionLocalProvider(
        LocalTodiNavigation provides navHostController
    ) {
        TodiTheme {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TODI",
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }
    }
}