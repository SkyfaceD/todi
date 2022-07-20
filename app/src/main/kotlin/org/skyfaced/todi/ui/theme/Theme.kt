package org.skyfaced.todi.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun TodiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    amoled: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context).copy(background = if (amoled) Color.Black else TodiDarkColorScheme.background) else dynamicLightColorScheme(
                context)
        }
        darkTheme -> TodiDarkColorScheme.copy(background = if (amoled) Color.Black else TodiDarkColorScheme.background)
        else -> TodiLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TodiTypography,
        shapes = TodiShapes,
        content = content,
    )
}