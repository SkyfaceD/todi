package org.skyfaced.todi.ui.screen.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import logcat.logcat
import org.skyfaced.todi.R
import org.skyfaced.todi.ui.util.TodiButton
import org.skyfaced.todi.ui.util.TodiTextField

@Composable
fun DetailsScreen(
    mode: Mode,
    id: Long? = null,
    viewModel: DetailsViewModel = viewModel()
) {
    DetailsScreen(
        state = viewModel.state,
        onTitleChange = viewModel::updateTitle,
        onDescriptionChange = viewModel::updateDescription
    )
}

@Composable
private fun DetailsScreen(
    state: DetailsUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TodiTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.title,
            onValueChange = onTitleChange,
            label = { Text(stringResource(R.string.lbl_title)) },
            singleLine = true
        )

        TodiTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            value = state.description,
            onValueChange = onDescriptionChange
        )

        TodiButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.lbl_add),
            onClick = {
                logcat { "OnClick" }
            }
        )
    }
}