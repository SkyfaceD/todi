package org.skyfaced.todi.util.fab

import androidx.compose.runtime.MutableState

class TodiExtendedFloatingActionButtonImpl(
    override val expanded: MutableState<Boolean>
) : TodiExtendedFloatingActionButton {
    override fun expand() {
        expanded.value = true
    }

    override fun shrink() {
        expanded.value = false
    }
}