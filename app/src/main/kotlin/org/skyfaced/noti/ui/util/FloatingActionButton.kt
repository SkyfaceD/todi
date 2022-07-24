package org.skyfaced.noti.ui.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/** @see enabled */
@Composable
fun ExtendedFloatingActionButton(
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    expanded: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
) {
    FloatingActionButton(
        modifier = modifier.sizeIn(
            minWidth = if (expanded) ExtendedFabMinimumWidth else ContainerWidth
        ),
        onClick = onClick,
        interactionSource = interactionSource,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        enabled = enabled
    ) {
        val startPadding = if (expanded) IconSize / 2 else 0.dp
        val endPadding = if (expanded) ExtendedFabTextPadding else 0.dp

        Row(
            modifier = Modifier.padding(start = startPadding, end = endPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            AnimatedVisibility(
                visible = expanded,
                enter = ExtendedFabExpandAnimation,
                exit = ExtendedFabCollapseAnimation,
            ) {
                Row {
                    Spacer(Modifier.width(ExtendedFabIconPadding))
                    text()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    content: @Composable () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = elevation.tonalElevation(interactionSource = interactionSource).value,
        shadowElevation = elevation.shadowElevation(interactionSource = interactionSource).value,
        interactionSource = interactionSource,
        enabled = enabled
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            // Adding the text style from [ExtendedFloatingActionButton] to all FAB variations. In
            // the majority of cases this will have no impact, because icons are expected, but if a
            // developer decides to put some short text to emulate an icon, (like "?") then it will
            // have the correct styling.
            ProvideTextStyle(
                MaterialTheme.typography.labelLarge,
            ) {
                Box(
                    modifier = Modifier
                        .defaultMinSize(
                            minWidth = ContainerWidth,
                            minHeight = ContainerHeight,
                        ),
                    contentAlignment = Alignment.Center,
                ) { content() }
            }
        }
    }
}

private val IconSize = 24.0.dp

private val ContainerWidth = 56.dp
private val ContainerHeight = 56.dp

private val ExtendedFabIconPadding = 12.dp
private val ExtendedFabTextPadding = 20.dp
private val ExtendedFabMinimumWidth = 80.dp

private val ExtendedFabCollapseAnimation = fadeOut(
    animationSpec = tween(
        durationMillis = 100,
        easing = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f),
    )
) + shrinkHorizontally(
    animationSpec = tween(
        durationMillis = 500,
        easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f),
    ),
    shrinkTowards = Alignment.Start,
)

private val ExtendedFabExpandAnimation = fadeIn(
    animationSpec = tween(
        durationMillis = 200,
        delayMillis = 100,
        easing = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f),
    ),
) + expandHorizontally(
    animationSpec = tween(
        durationMillis = 500,
        easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f),
    ),
    expandFrom = Alignment.Start,
)

