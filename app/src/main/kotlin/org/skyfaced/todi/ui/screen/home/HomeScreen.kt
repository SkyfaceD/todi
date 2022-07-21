package org.skyfaced.todi.ui.screen.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.skyfaced.todi.R
import org.skyfaced.todi.ui.model.note.Note
import org.skyfaced.todi.ui.screen.Screens
import org.skyfaced.todi.ui.screen.details.Mode
import org.skyfaced.todi.ui.util.ContentPadding
import org.skyfaced.todi.ui.util.LazyStaggeredGrid
import org.skyfaced.todi.ui.util.ScreenState
import org.skyfaced.todi.util.LocalTodiNavigation
import org.skyfaced.todi.util.exception.FlowException

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
) {
    val navHostController = LocalTodiNavigation.current

    HomeScreen(
        state = viewModel.state,
        onItemClick = { note ->
            navHostController.navigate(Screens.Details.argRoute(Mode.Edit, note.id))
        },
        onDeleteItemClick = viewModel::deleteNote,
        onRefresh = viewModel::refresh
    )
}

// TODO Laggy when switch between screen states
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    state: HomeUiState,
    onItemClick: (Note) -> Unit,
    onDeleteItemClick: (Note) -> Unit,
    onRefresh: () -> Unit,
) {
    val screenState = when {
        state.isLoading -> ScreenState.Loading
        !state.isLoading && state.notes != null && state.notes.isNotEmpty() -> ScreenState.Success
        !state.isLoading && state.notes != null && state.notes.isEmpty() -> ScreenState.Empty
        !state.isLoading && state.uiMessage != null && state.uiMessage.cause is FlowException -> ScreenState.Failure
        else -> ScreenState.Unknown
    }

    Crossfade(targetState = screenState) { targetValue ->
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
                    Text(text = stringResource(R.string.msg_no_notes))
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
                        painter = painterResource(R.drawable.ic_note), // Replace icon
                        contentDescription = null
                    )
                    val message = state.uiMessage?.messageRes ?: R.string.msg_unexpected_exception
                    Text(text = stringResource(message))
                    Button(modifier = Modifier.widthIn(min = 100.dp), onClick = onRefresh) {
                        Text(stringResource(R.string.lbl_try_again))
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
                    if (state.gridCells > 1) {
                        LazyStaggeredGrid(
                            columnCount = 2,
                            contentPadding = ContentPadding(16.dp),
                        ) {
                            notes.forEach { item ->
                                item {
                                    Column {
                                        Item(
                                            item = item,
                                            descriptionMaxLines = state.descriptionMaxLines,
                                            onItemClick = { onItemClick(item) },
                                            onDeleteItemClick = { onDeleteItemClick(item) }
                                        )
                                        Spacer(Modifier.height(16.dp))
                                    }
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(items = notes, key = { it.id }) { item ->
                                Item(
                                    modifier = Modifier.animateItemPlacement(),
                                    item = item,
                                    descriptionMaxLines = state.descriptionMaxLines,
                                    onItemClick = { onItemClick(item) },
                                    onDeleteItemClick = { onDeleteItemClick(item) }
                                )
                            }
                        }
                    }
                }
            }
            ScreenState.Unknown -> { /* Ignore */
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
                    border = BorderStroke(0.dp, Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.cd_delete)
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