package org.skyfaced.todi.ui.screen.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import org.skyfaced.todi.BuildConfig
import org.skyfaced.todi.R
import org.skyfaced.todi.settings.TodiLocale
import org.skyfaced.todi.settings.TodiTheme
import org.skyfaced.todi.ui.util.ConfirmationData
import org.skyfaced.todi.ui.util.ConfirmationDialog
import org.skyfaced.todi.ui.util.VerticalDivider
import org.skyfaced.todi.util.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
) {
    val context = LocalContext.current

    val state = viewModel.state.collectAsStateWithLifecycle(initial = null).value

    state?.let {
        SettingsScreen(
            state = state,
            onThemeChange = viewModel::updateTheme,
            onDynamicColorChange = viewModel::updateDynamicColor,
            onAmoledChange = viewModel::updateAmoled,
            onLocaleChange = viewModel::updateLocale,
            onExternalLinkClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(context, intent, null)
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SettingsScreen(
    state: SettingsUiState,
    onThemeChange: (TodiTheme) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    onAmoledChange: (Boolean) -> Unit,
    onLocaleChange: (TodiLocale) -> Unit,
    onExternalLinkClick: (String) -> Unit,
) {
    val themeDialog = remember { mutableStateOf(false) }
    val languageDialog = remember { mutableStateOf(false) }
    val aboutDialog = remember { mutableStateOf(false) }

    Dialogs(
        state = state,
        themeDialog = themeDialog,
        languageDialog = languageDialog,
        aboutDialog = aboutDialog,
        onThemeChange = {
            onThemeChange(it)
            if (it == TodiTheme.Light) onAmoledChange(false)
        },
        onLocaleChange = onLocaleChange,
        onExternalLinkClick = onExternalLinkClick
    )

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        val (themeSummary, themeIcon) = when (state.theme) {
            TodiTheme.System -> R.string.lbl_system_theme to R.drawable.ic_theme_system
            TodiTheme.Light -> R.string.lbl_theme_light to R.drawable.ic_theme_light
            TodiTheme.Dark -> R.string.lbl_theme_dark to R.drawable.ic_theme_dark
        }
        SettingsButton(
            title = stringResource(R.string.lbl_theme),
            summary = stringResource(themeSummary),
            icon = painterResource(themeIcon),
            onClick = { themeDialog.value = true }
        )

        AnimatedVisibility(
            visible = state.theme == TodiTheme.Dark || state.theme == TodiTheme.System && isSystemInDarkTheme(),
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            SettingsSwitcher(
                title = stringResource(R.string.lbl_amoled_support),
                subtitle = stringResource(R.string.lbl_amoled_support_summary),
                icon = painterResource(R.drawable.ic_amoled),
                checked = state.amoled,
                onClick = { onAmoledChange(!state.amoled) },
                enabled = state.theme == TodiTheme.Dark || state.theme == TodiTheme.System && isSystemInDarkTheme()
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
            TodiLocale.English -> R.string.lbl_language_english
            TodiLocale.Russian -> R.string.lbl_language_russian
        }
        SettingsButton(
            title = stringResource(R.string.lbl_language),
            summary = stringResource(languageSummary),
            icon = painterResource(R.drawable.ic_language),
            onClick = { languageDialog.value = true },
        )

        SettingsButton(
            title = stringResource(R.string.lbl_about_app),
            summary = null,
            icon = painterResource(R.drawable.ic_info),
            onClick = { aboutDialog.value = true },
        )
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
    asImage: Boolean = false,
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
        if (asImage) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = icon,
                contentDescription = null,
            )
        } else {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = icon,
                contentDescription = null,
            )
        }
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
    aboutDialog: MutableState<Boolean>,
    onThemeChange: (TodiTheme) -> Unit,
    onLocaleChange: (TodiLocale) -> Unit,
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
                onThemeChange(TodiTheme.values()[pos])
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
                onLocaleChange(TodiLocale.from(pos))
            }
        )
    }

    if (aboutDialog.value) {
        //@formatter:off
        val items = listOf(
            ExternalLinkData(R.drawable.ic_4pda, R.string.cd_4pda, "https://4pda.to/"),
            ExternalLinkData(R.drawable.ic_google_play, R.string.cd_google_play, "https://play.google.com/store/apps"),
            ExternalLinkData(R.drawable.ic_github, R.string.cd_github, "https://github.com/SkyfaceD/todi"),
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