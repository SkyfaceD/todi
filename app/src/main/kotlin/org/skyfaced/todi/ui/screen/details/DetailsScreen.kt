package org.skyfaced.todi.ui.screen.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.skyfaced.todi.R
import org.skyfaced.todi.ui.util.TodiButton
import org.skyfaced.todi.ui.util.TodiTextField
import org.skyfaced.todi.util.LocalTodiNavigation

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = viewModel(),
) {
    val navHostController = LocalTodiNavigation.current

    DetailsScreen(
        state = viewModel.state,
        onTitleChange = viewModel::updateTitle,
        onDescriptionChange = viewModel::updateDescription,
        onButtonClick = viewModel::upsert,
        onMessageShown = viewModel::clearMessage,
        navigateUp = navHostController::navigateUp
    )
}

@Composable
private fun DetailsScreen(
    state: DetailsUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    onMessageShown: (Long) -> Unit,
    navigateUp: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.upserted) {
        if (state.upserted) navigateUp()
    }

    state.uiMessage?.let { uiMessage ->
        val text = stringResource(uiMessage.messageRes)
        LaunchedEffect(uiMessage) {
            snackbarHostState.showSnackbar(text)
            onMessageShown(uiMessage.id)
        }
    }

    Box {
        SnackbarHost(hostState = snackbarHostState)

        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TodiTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                onValueChange = onTitleChange,
                label = { Text(stringResource(R.string.lbl_title)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
            )

            TodiTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                value = state.description,
                onValueChange = onDescriptionChange,
                label = { Text(stringResource(R.string.lbl_description)) },
            )

            state.mode?.let {
                val text = when (state.mode) {
                    Mode.Edit -> R.string.lbl_save
                    Mode.Create -> R.string.lbl_add
                }
                TodiButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(text),
                    onClick = onButtonClick
                )
            }
        }
    }
}