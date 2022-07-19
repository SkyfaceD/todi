package org.skyfaced.todi.ui.util

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalTodiNavigation = staticCompositionLocalOf<NavHostController> { error("Not provided") }