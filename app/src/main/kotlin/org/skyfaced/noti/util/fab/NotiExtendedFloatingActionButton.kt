package org.skyfaced.noti.util.fab

import androidx.compose.runtime.MutableState

interface NotiExtendedFloatingActionButton {
    val expanded: MutableState<Boolean>

    fun expand()

    fun shrink()
}