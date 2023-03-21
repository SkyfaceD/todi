package org.skyfaced.noti.ui.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
import org.skyfaced.noti.R

@Composable
fun ConfirmationDialog(
    title: String,
    items: List<ConfirmationData>,
    onDismissRequest: () -> Unit,
    onConfirm: (pos: Int, item: ConfirmationData) -> Unit,
    defaultSelectedPosition: Int? = null,
    immediatelyConfirm: Boolean = true,
    properties: DialogProperties = DialogProperties(),
) {
    val context = LocalContext.current
    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()

    var selectedPos by remember { mutableStateOf(defaultSelectedPosition) }
    var selectedItem by remember { mutableStateOf(items.getOrNull(selectedPos ?: -1)) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Surface(shape = MaterialTheme.shapes.extraLarge, tonalElevation = 6.dp) {
            Box {
                Column(
                    modifier = Modifier
                        .sizeIn(minWidth = MinWidth, maxWidth = MaxWidth)
                        .padding(DialogPadding)
                ) {
                    Text(
                        modifier = Modifier.padding(TitlePadding),
                        text = title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    LazyColumn {
                        itemsIndexed(items) { idx, item ->
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        selectedPos = idx
                                        selectedItem = item

                                        if (immediatelyConfirm) onConfirm(idx, item)
                                    }
                                    .padding(vertical = 16.dp, horizontal = 24.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = idx == selectedPos,
                                    onClick = null,
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = item.title)
                                    item.summary?.let {
                                        Text(
                                            text = item.summary,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(ButtonPadding)
                    ) {
                        TextButton(onDismissRequest) { Text(stringResource(R.string.lbl_cancel)) }
                        if (!immediatelyConfirm) {
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    if (selectedPos != null && selectedItem != null) {
                                        onConfirm(selectedPos!!, selectedItem!!)
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(context.getString(R.string.msg_select_one_item))
                                        }
                                    }
                                }
                            ) {
                                Text(stringResource(R.string.lbl_ok))
                            }
                        }
                    }
                }
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

data class ConfirmationData(
    val title: String,
    val summary: String? = null,
)

private val DialogPadding = PaddingValues(top = 24.dp, bottom = 18.dp)
private val TitlePadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 16.dp)
private val ButtonPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp)

private val MinWidth = 280.dp
private val MaxWidth = 560.dp