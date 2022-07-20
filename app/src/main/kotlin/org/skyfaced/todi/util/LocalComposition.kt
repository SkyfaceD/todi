package org.skyfaced.todi.util

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import org.skyfaced.todi.database.TodiDatabase
import org.skyfaced.todi.settings.TodiSettings

val LocalTodiNavigation = staticCompositionLocalOf<NavHostController> { error("Not provided") }

val LocalTodiSettings = staticCompositionLocalOf<TodiSettings> { error("Not provided") }

val LocalTodiDatabase = staticCompositionLocalOf<TodiDatabase> { error("Not provided") }