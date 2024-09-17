package org.skyfaced.noti.ui.screen.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.skyfaced.noti.R
import org.skyfaced.noti.settings.locale.strings
import org.skyfaced.noti.ui.model.note.Note
import org.skyfaced.noti.ui.screen.Screens
import org.skyfaced.noti.util.ContentPadding
import org.skyfaced.noti.util.LocalNotiNavigation
import org.skyfaced.noti.util.LocalNotiNotifications
import org.skyfaced.noti.util.Mode
import org.skyfaced.noti.util.ScreenState
import org.skyfaced.noti.util.exception.FlowException

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
) {
    val navHostController = LocalNotiNavigation.current
    val notifications = LocalNotiNotifications.current

    HomeScreen(
        state = viewModel.state,
        onItemClick = { note ->
            notifications.hideSnackbar()
            navHostController.navigate(Screens.Details.argRoute(Mode.Edit, note.id))
        },
        onDeleteItemClick = viewModel::deleteNote,
        onRefresh = viewModel::refresh,
        onMessageShown = viewModel::clearMessage,
        onUndoClick = viewModel::undo
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    state: HomeUiState,
    onItemClick: (Note) -> Unit,
    onDeleteItemClick: (Note) -> Unit,
    onRefresh: () -> Unit,
    onMessageShown: (Long) -> Unit,
    onUndoClick: (Note) -> Unit,
) {
    val notifications = LocalNotiNotifications.current

    val undo = strings.lbl_undo

    state.uiMessage?.let { uiMessage ->
        LaunchedEffect(uiMessage) {
            notifications.showSnackbar(
                message = uiMessage.message.orEmpty(),
                actionLabel = if (uiMessage.cause !is FlowException) undo else null,
                withDismissAction = true
            ).also {
                if (it == SnackbarResult.ActionPerformed) {
                    onUndoClick(uiMessage.data as Note)
                }
            }
            onMessageShown(uiMessage.id)
        }
    }

    val screenState = when {
        state.isLoading -> ScreenState.Loading
        !state.notes.isNullOrEmpty() -> ScreenState.Success
        state.notes != null -> ScreenState.Empty
        state.uiMessage != null && state.uiMessage.cause is FlowException -> ScreenState.Failure
        else -> ScreenState.Unknown
    }

    Crossfade(targetState = screenState, label = "crossfadeAnimation") { targetValue ->
        when (targetValue) {
            ScreenState.Empty -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(128.dp),
                        painter = painterResource(R.drawable.ic_note),
                        contentDescription = null
                    )
                    Text(text = strings.msg_no_notes)
                }
            }
            ScreenState.Failure -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(128.dp),
                        painter = painterResource(R.drawable.ic_error),
                        contentDescription = null
                    )
                    val message = state.uiMessage?.message ?: strings.msg_unexpected_exception
                    Text(text = message)
                    Button(modifier = Modifier.widthIn(min = 100.dp), onClick = onRefresh) {
                        Text(strings.lbl_try_again)
                    }
                }
            }
            ScreenState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
            ScreenState.Success -> {
                state.notes?.let { notes ->
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(state.gridCells),
                        contentPadding = ContentPadding(16.dp).copy(bottom = 96.dp)
                            .toPaddingValues(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(items = notes, key = { it.id }) { item ->
                            Item(
                                modifier = Modifier.animateItemPlacement(),
                                item = item,
                                descriptionMaxLines = state.descriptionMaxLines,
                                onItemClick = { onItemClick(item) },
                                onDeleteItemClick = {
                                    notifications.hideSnackbar()
                                    onDeleteItemClick(item)
                                }
                            )
                        }
                    }
                }
            }
            ScreenState.Unknown -> {
                /* Ignore */
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Item(
    item: Note,
    descriptionMaxLines: Int,
    onItemClick: () -> Unit,
    onDeleteItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        onClick = onItemClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    text = item.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge
                )
                OutlinedButton(
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    onClick = onDeleteItemClick,
                    border = null,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = strings.cd_delete
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                text = item.description,
                maxLines = descriptionMaxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}