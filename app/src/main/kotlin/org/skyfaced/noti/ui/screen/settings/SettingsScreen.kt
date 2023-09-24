package org.skyfaced.noti.ui.screen.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.skyfaced.noti.BuildConfig
import org.skyfaced.noti.R
import org.skyfaced.noti.settings.NotiLocale
import org.skyfaced.noti.settings.NotiTheme
import org.skyfaced.noti.ui.theme.ClearRippleTheme
import org.skyfaced.noti.ui.util.ConfirmationData
import org.skyfaced.noti.ui.util.ConfirmationDialog
import org.skyfaced.noti.ui.util.VerticalDivider
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
) {
    val context = LocalContext.current

    val state = viewModel.state.collectAsStateWithLifecycle(null).value

    state?.let {
        SettingsScreen(
            state = state,
            onThemeChange = viewModel::updateTheme,
            onDynamicColorChange = viewModel::updateDynamicColor,
            onAmoledChange = viewModel::updateAmoled,
            onLocaleChange = viewModel::updateLocale,
            onGridCellsChange = viewModel::updateGridCells,
            onExternalLinkClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(context, intent, null)
            },
        )
    }
}

@Composable
private fun SettingsScreen(
    state: SettingsUiState,
    onThemeChange: (NotiTheme) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    onAmoledChange: (Boolean) -> Unit,
    onLocaleChange: (NotiLocale) -> Unit,
    onGridCellsChange: (Int) -> Unit,
    onExternalLinkClick: (String) -> Unit,
) {
    val themeDialog = remember { mutableStateOf(false) }
    val languageDialog = remember { mutableStateOf(false) }
    val viewDialog = remember { mutableStateOf(false) }
    val aboutDialog = remember { mutableStateOf(false) }

    Dialogs(
        state = state,
        themeDialog = themeDialog,
        languageDialog = languageDialog,
        viewDialog = viewDialog,
        aboutDialog = aboutDialog,
        onThemeChange = {
            onThemeChange(it)
            if (it == NotiTheme.Light) onAmoledChange(false)
        },
        onLocaleChange = onLocaleChange,
        onViewChange = onGridCellsChange,
        onExternalLinkClick = onExternalLinkClick
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        val (themeSummary, themeIcon) = when (state.theme) {
            NotiTheme.System -> R.string.lbl_system_theme to R.drawable.ic_theme_system
            NotiTheme.Light -> R.string.lbl_theme_light to R.drawable.ic_theme_light
            NotiTheme.Dark -> R.string.lbl_theme_dark to R.drawable.ic_theme_dark
        }
        SettingsButton(
            title = stringResource(R.string.lbl_theme),
            summary = stringResource(themeSummary),
            icon = painterResource(themeIcon),
            onClick = { themeDialog.value = true }
        )

        AnimatedVisibility(
            visible = state.theme == NotiTheme.Dark || state.theme == NotiTheme.System && isSystemInDarkTheme(),
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            SettingsSwitcher(
                title = stringResource(R.string.lbl_amoled_support),
                subtitle = stringResource(R.string.lbl_amoled_support_summary),
                icon = painterResource(R.drawable.ic_amoled),
                checked = state.amoled,
                onClick = { onAmoledChange(!state.amoled) },
                enabled = state.theme == NotiTheme.Dark || state.theme == NotiTheme.System && isSystemInDarkTheme()
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SettingsSwitcher(
                title = stringResource(R.string.lbl_dynamic_theme),
                subtitle = stringResource(R.string.lbl_dynamic_theme_summary),
                icon = painterResource(R.drawable.ic_palette),
                checked = state.dynamicColor,
                onClick = { onDynamicColorChange(!state.dynamicColor) }
            )
        }

        val languageSummary = when (state.locale) {
            NotiLocale.English -> R.string.lbl_language_english
            NotiLocale.Russian -> R.string.lbl_language_russian
        }
        SettingsButton(
            title = stringResource(R.string.lbl_language),
            summary = stringResource(languageSummary),
            icon = painterResource(R.drawable.ic_language),
            onClick = { languageDialog.value = true },
        )

        val (viewTypeSummary, viewIcon) = if (state.gridCells > 1) R.string.lbl_view_grid to R.drawable.ic_view_grid
        else R.string.lbl_view_linear to R.drawable.ic_view_linear
        SettingsButton(
            title = stringResource(R.string.lbl_view_type),
            summary = stringResource(viewTypeSummary),
            icon = painterResource(viewIcon),
            onClick = { viewDialog.value = true }
        )

        AnimatedVisibility(
            visible = state.gridCells > 1 && booleanResource(R.bool.isTablet),
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            SettingsSlider(
                title = stringResource(R.string.lbl_column_number),
                summary = state.gridCells.toString(),
                icon = painterResource(R.drawable.ic_column),
                value = state.gridCells.toFloat(),
                onValueChange = { onGridCellsChange(it.roundToInt()) },
                valueRange = 2f..5f,
                steps = 2,
                enabled = state.gridCells > 1,
            )
        }

        SettingsButton(
            title = stringResource(R.string.lbl_about_app),
            summary = null,
            icon = painterResource(R.drawable.ic_info),
            onClick = { aboutDialog.value = true },
        )
    }
}

@Composable
private fun SettingsSlider(
    title: String,
    summary: String,
    icon: Painter,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
) {
    TextButton(
        onClick = {},
        shape = RectangleShape,
        enabled = enabled,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
            )
            Spacer(Modifier.width(24.dp))
            Column {
                Text(title)
                Text(text = summary, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.width(16.dp))
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                steps = steps,
                enabled = enabled,
            )
        }
    }
}

@Composable
private fun SettingsSwitcher(
    title: String,
    subtitle: String,
    icon: Painter,
    checked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }

    TextButton(
        onClick = { onClick() },
        shape = RectangleShape,
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
            )
            Spacer(Modifier.width(24.dp))
            Column(Modifier.weight(1f)) {
                Text(title)
                Text(text = subtitle, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.width(8.dp))
            CompositionLocalProvider(
                LocalRippleTheme provides ClearRippleTheme
            ) {
                Switch(
                    checked = checked,
                    onCheckedChange = null,
                    enabled = enabled,
                    interactionSource = interactionSource
                )
            }
        }
    }
}

@Composable
private fun SettingsButton(
    title: String,
    summary: String?,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = TextButton(
    onClick = onClick,
    shape = RectangleShape,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = icon,
            contentDescription = null,
        )
        Spacer(Modifier.width(24.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            summary?.let {
                Text(text = summary, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun Dialogs(
    state: SettingsUiState,
    themeDialog: MutableState<Boolean>,
    languageDialog: MutableState<Boolean>,
    viewDialog: MutableState<Boolean>,
    aboutDialog: MutableState<Boolean>,
    onThemeChange: (NotiTheme) -> Unit,
    onLocaleChange: (NotiLocale) -> Unit,
    onViewChange: (Int) -> Unit,
    onExternalLinkClick: (String) -> Unit,
) {
    if (themeDialog.value) {
        ConfirmationDialog(
            title = stringResource(R.string.lbl_select_theme),
            items = listOf(
                ConfirmationData(stringResource(R.string.lbl_system_theme)),
                ConfirmationData(stringResource(R.string.lbl_theme_light)),
                ConfirmationData(stringResource(R.string.lbl_theme_dark)),
            ),
            defaultSelectedPosition = state.theme.ordinal,
            onDismissRequest = { themeDialog.value = false },
            onConfirm = { pos, _ ->
                themeDialog.value = false
                onThemeChange(NotiTheme.entries[pos])
            }
        )
    }

    if (languageDialog.value) {
        ConfirmationDialog(
            title = stringResource(R.string.lbl_select_language),
            items = listOf(
                //@formatter:off
                ConfirmationData(stringResource(R.string.lbl_language_english_native),stringResource(R.string.lbl_language_english)),
                ConfirmationData(stringResource(R.string.lbl_language_russian_native),stringResource(R.string.lbl_language_russian))
                //@formatter:on
            ),
            defaultSelectedPosition = state.locale.ordinal,
            onDismissRequest = { languageDialog.value = false },
            onConfirm = { pos, _ ->
                languageDialog.value = false
                onLocaleChange(NotiLocale.from(pos))
            }
        )
    }

    if (viewDialog.value) {
        ConfirmationDialog(
            title = stringResource(R.string.lbl_view_select_list_type),
            items = listOf(
                ConfirmationData(stringResource(R.string.lbl_view_linear)),
                ConfirmationData(stringResource(R.string.lbl_view_grid))
            ),
            defaultSelectedPosition = if (state.gridCells > 1) 1 else 0,
            onDismissRequest = { viewDialog.value = false },
            onConfirm = { pos, _ ->
                viewDialog.value = false
                onViewChange(pos.inc())
            }
        )
    }

    if (aboutDialog.value) {
        //@formatter:off
        val items = listOf(
            ExternalLinkData(R.drawable.ic_google_play, R.string.cd_google_play, "https://play.google.com/store/apps/details?id=org.skyfaced.noti&pcampaignid=web_share"),
            ExternalLinkData(R.drawable.ic_huawei, R.string.cd_huawei, "https://appgallery.huawei.com/app/C106876647"),
            ExternalLinkData(R.drawable.ic_amazon, R.string.cd_amazon, "https://www.amazon.com/dp/B0B9NZ9X1G/ref=sr_1_2?crid=E70Z7LWY9ULN&keywords=noti+app&qid=1661438584&sprefix=noti+ap%2Caps%2C252&sr=8-2"),
            ExternalLinkData(R.drawable.ic_github, R.string.cd_github, "https://github.com/SkyfaceD/noti"),
        )
        //@formatter:on

        AlertDialog(
            onDismissRequest = { aboutDialog.value = false },
            title = { Text(stringResource(R.string.lbl_about_app)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.lbl_app_name).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(R.string.placeholder_app_version, BuildConfig.VERSION_NAME))
                    Text(stringResource(R.string.lbl_external_links))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        itemsIndexed(items) { idx, item ->
                            IconButton(
                                onClick = { onExternalLinkClick(item.link) }
                            ) {
                                Icon(
                                    painterResource(item.icon),
                                    stringResource(item.contentDescription)
                                )
                            }

                            if (idx < items.lastIndex) {
                                Spacer(Modifier.width(4.dp))
                                VerticalDivider(24.dp)
                                Spacer(Modifier.width(4.dp))
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { aboutDialog.value = false }) {
                    Text(stringResource(R.string.lbl_ok))
                }
            }
        )
    }
}

data class ExternalLinkData(
    @DrawableRes
    val icon: Int,
    @StringRes
    val contentDescription: Int,
    val link: String,
)