package org.skyfaced.todi.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class ContentPadding(
    @Stable
    val start: Dp = 0.dp,
    @Stable
    val top: Dp = 0.dp,
    @Stable
    val end: Dp = 0.dp,
    @Stable
    val bottom: Dp = 0.dp,
) {
    constructor(all: Dp = 0.dp) : this(all, all, all, all)

    fun toPaddingValues() = PaddingValues(start, top, end, bottom)
}