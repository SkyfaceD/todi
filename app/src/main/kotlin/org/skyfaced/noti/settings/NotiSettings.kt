package org.skyfaced.noti.settings

interface NotiSettings {
    val theme: Settings<NotiTheme>
    val dynamicColor: Settings<Boolean>
    val amoled: Settings<Boolean>
    val locale: Settings<NotiLocale>
    val gridCells: Settings<Int>
}