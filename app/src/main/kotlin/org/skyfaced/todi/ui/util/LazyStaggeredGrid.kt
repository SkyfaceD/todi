package org.skyfaced.todi.ui.util

import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LazyStaggeredGrid(
    columnCount: Int,
    contentPadding: ContentPadding = ContentPadding(0.dp),
    gap: Dp = contentPadding.end / 2,
    content: @Composable LazyStaggeredGridScope.() -> Unit,
) {
    val states: Array<LazyListState> = (0 until columnCount)
        .map { rememberLazyListState() }
        .toTypedArray()
    val scope = rememberCoroutineScope { Dispatchers.Main.immediate }
    val scroll = rememberScrollableState { delta ->
        scope.launch { states.forEach { it.scrollBy(-delta) } }
        delta
    }
    val gridScope = LazyStaggeredGridScope(columnCount)
    content(gridScope)

    Box(
        modifier = Modifier.scrollable(
            state = scroll,
            orientation = Orientation.Vertical,
            flingBehavior = ScrollableDefaults.flingBehavior()
        )
    ) {
        Row {
            for (index in 0 until columnCount) {
                LazyColumn(
                    userScrollEnabled = false,
                    contentPadding = contentPadding.copy(
                        start = if (index == 0) contentPadding.start else 0.dp,
                        end = if (index == columnCount.dec()) contentPadding.end else 0.dp,
                    ).toPaddingValues(),
                    state = states[index],
                    modifier = Modifier.weight(1f)
                ) {
                    for ((key, itemContent) in gridScope.items[index]) {
                        item(key = key) {
                            itemContent()
                        }
                    }
                }
                if (index < columnCount.dec()) Spacer(Modifier.width(gap))
            }
        }
    }
}

class LazyStaggeredGridScope(private val columnCount: Int) {
    var currentIndex = 0
    val items: Array<MutableList<Pair<Any?, @Composable () -> Unit>>> =
        Array(columnCount) { mutableListOf() }

    fun item(key: Any? = null, content: @Composable () -> Unit) {
        items[currentIndex % columnCount] += key to content
        currentIndex += 1
    }
}

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