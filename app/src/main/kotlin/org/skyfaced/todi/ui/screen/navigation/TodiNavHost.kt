package org.skyfaced.todi.ui.screen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.skyfaced.todi.ui.screen.details.DetailsScreen
import org.skyfaced.todi.ui.screen.home.HomeScreen
import org.skyfaced.todi.ui.util.LocalTodiNavigation
import org.skyfaced.todi.ui.screen.details.Mode
import org.skyfaced.todi.ui.screen.settings.SettingsScreen
import java.util.*

@Composable
fun TodiNavHost(
    modifier: Modifier = Modifier,
) {
    val navHostController = LocalTodiNavigation.current

    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Screens.Home.route
    ) {
        composable(Screens.Home.route) {
            HomeScreen()
        }

        composable(Screens.Details.route) { navBackStackEntry ->
            val uuid = UUID.fromString(navBackStackEntry.arguments?.getString("uuid").orEmpty())
            val mode = Mode.valueOf(navBackStackEntry.arguments?.getString("mode").orEmpty())

            DetailsScreen(
                uuid = uuid,
                mode = mode,
            )
        }

        composable(Screens.Settings.route) {
            SettingsScreen()
        }
    }
}