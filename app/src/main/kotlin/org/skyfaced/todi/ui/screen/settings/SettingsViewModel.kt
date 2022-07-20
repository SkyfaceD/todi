package org.skyfaced.todi.ui.screen.settings

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.skyfaced.todi.settings.TodiLocale
import org.skyfaced.todi.settings.TodiSettings
import org.skyfaced.todi.settings.TodiTheme

class SettingsViewModel(
    private val settings: TodiSettings,
) : ViewModel() {
    val state = combine(
        settings.theme.observe,
        settings.dynamicColor.observe,
        settings.amoled.observe,
        settings.locale.observe
    ) { theme, dynamicColor, amoled, locale ->
        SettingsUiState(theme, dynamicColor, amoled, locale)
    }

    fun updateTheme(theme: TodiTheme) {
        viewModelScope.launch { settings.theme(theme) }
    }

    fun updateDynamicColor(dynamicColor: Boolean) {
        viewModelScope.launch { settings.dynamicColor(dynamicColor) }
    }

    fun updateAmoled(amoled: Boolean) {
        viewModelScope.launch { settings.amoled(amoled) }
    }

    fun updateLocale(locale: TodiLocale) {
        viewModelScope.launch { settings.locale(locale) }
    }
}

@Immutable
data class SettingsUiState(
    val theme: TodiTheme = TodiTheme.System,
    val dynamicColor: Boolean = true,
    val amoled: Boolean = false,
    val locale: TodiLocale = TodiLocale.English,
)
