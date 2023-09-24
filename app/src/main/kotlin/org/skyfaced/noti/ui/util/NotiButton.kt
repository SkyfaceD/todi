package org.skyfaced.noti.ui.util

import androidx.compose.foundation.clickable
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun NotiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NotiTextField(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .clickable { onClick() },
        value = text,
        onValueChange = {},
        readOnly = true,
        enabled = false,
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        colors = TextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}