package org.skyfaced.todi.ui.screen

import org.skyfaced.todi.ui.screen.details.Mode
import java.util.*

sealed class Screens(val route: String) {
    object Home : Screens("home")

    object Details : Screens("home/details/{uuid}/{mode}") {
        fun argRoute(uuid: UUID, mode: Mode): String {
            return "home/details/$uuid/${mode.name}"
        }
    }

    object Settings : Screens("home/settings")
}