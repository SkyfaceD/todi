package org.skyfaced.noti.util.fab

import androidx.compose.runtime.MutableState

class NotiExtendedFloatingActionButtonImpl(
    override val expanded: MutableState<Boolean>
) : NotiExtendedFloatingActionButton {
    override fun expand() {
        expanded.value = true
    }

    override fun shrink() {
        expanded.value = false
    }
}