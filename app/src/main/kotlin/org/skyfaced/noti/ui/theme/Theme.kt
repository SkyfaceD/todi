package org.skyfaced.noti.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun NotiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    amoled: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context).copy(background = if (amoled) Color.Black else NotiDarkColorScheme.background) else dynamicLightColorScheme(
                context
            )
        }
        darkTheme -> NotiDarkColorScheme.copy(background = if (amoled) Color.Black else NotiDarkColorScheme.background)
        else -> NotiLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NotiTypography,
        shapes = NotiShapes,
        content = content,
    )
}

/** @see <a href="https://stackoverflow.com/a/69784868/9846834">Class source code</a> */
object ClearRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color = Color.Transparent

    @Composable
    override fun rippleAlpha() = RippleAlpha(
        draggedAlpha = 0.0f,
        focusedAlpha = 0.0f,
        hoveredAlpha = 0.0f,
        pressedAlpha = 0.0f,
    )
}