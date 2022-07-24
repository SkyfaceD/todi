package org.skyfaced.noti.ui.screen.settings

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.skyfaced.noti.settings.NotiLocale
import org.skyfaced.noti.settings.NotiSettings
import org.skyfaced.noti.settings.NotiTheme

class SettingsViewModel(
    private val settings: NotiSettings,
) : ViewModel() {
    val state = combine(
        settings.theme.observe,
        settings.dynamicColor.observe,
        settings.amoled.observe,
        settings.locale.observe,
        settings.gridCells.observe
    ) { theme, dynamicColor, amoled, locale, gridCells ->
        SettingsUiState(theme, dynamicColor, amoled, locale, gridCells)
    }

    fun updateTheme(theme: NotiTheme) {
        viewModelScope.launch { settings.theme(theme) }
    }

    fun updateDynamicColor(dynamicColor: Boolean) {
        viewModelScope.launch { settings.dynamicColor(dynamicColor) }
    }

    fun updateAmoled(amoled: Boolean) {
        viewModelScope.launch { settings.amoled(amoled) }
    }

    fun updateLocale(locale: NotiLocale) {
        viewModelScope.launch { settings.locale(locale) }
    }

    fun updateGridCells(gridCells: Int) {
        viewModelScope.launch { settings.gridCells(gridCells) }
    }
}

@Immutable
data class SettingsUiState(
    val theme: NotiTheme,
    val dynamicColor: Boolean,
    val amoled: Boolean,
    val locale: NotiLocale,
    val gridCells: Int
)
