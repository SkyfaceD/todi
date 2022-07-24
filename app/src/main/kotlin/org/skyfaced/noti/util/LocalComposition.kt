package org.skyfaced.noti.util

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import org.skyfaced.noti.database.NotiDatabase
import org.skyfaced.noti.settings.NotiSettings
import org.skyfaced.noti.util.fab.NotiExtendedFloatingActionButton
import org.skyfaced.noti.util.notifcations.NotiNotifications

val LocalNotiNavigation = staticCompositionLocalOf<NavHostController> { error("Not provided") }

val LocalNotiSettings = staticCompositionLocalOf<NotiSettings> { error("Not provided") }

val LocalNotiDatabase = staticCompositionLocalOf<NotiDatabase> { error("Not provided") }

val LocalNotiNotifications = staticCompositionLocalOf<NotiNotifications> { error("Not provided") }

val LocalNotiExtendedFloatingActionButton =
    staticCompositionLocalOf<NotiExtendedFloatingActionButton> { error("Not provided") }