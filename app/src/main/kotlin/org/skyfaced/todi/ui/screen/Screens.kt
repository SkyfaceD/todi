package org.skyfaced.todi.ui.screen

import org.skyfaced.todi.util.Mode

sealed class Screens(val route: String) {
    object Home : Screens("home")

    object Details : Screens("home/details/{mode}?id={id}") {
        fun argRoute(mode: Mode, id: Long? = null): String {
            return "home/details/${mode.name}?id=$id"
        }
    }

    object Settings : Screens("home/settings")
}