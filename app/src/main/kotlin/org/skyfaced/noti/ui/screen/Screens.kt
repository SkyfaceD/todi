package org.skyfaced.noti.ui.screen

import org.skyfaced.noti.util.Mode

sealed class Screens(val route: String) {
    data object Home : Screens("home")

    data object Details : Screens("home/details/{mode}?id={id}") {
        fun argRoute(mode: Mode, id: Long? = null): String {
            return "home/details/${mode.name}?id=$id"
        }
    }

    data object Settings : Screens("home/settings")
}