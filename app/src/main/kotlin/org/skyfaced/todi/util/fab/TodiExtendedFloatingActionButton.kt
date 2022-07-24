package org.skyfaced.todi.util.fab

import androidx.compose.runtime.MutableState

interface TodiExtendedFloatingActionButton {
    val expanded: MutableState<Boolean>

    fun expand()

    fun shrink()
}