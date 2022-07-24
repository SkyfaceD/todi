package org.skyfaced.noti.settings

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import logcat.logcat
import org.skyfaced.noti.R
import java.io.IOException

class NotiSettingsImpl(
    private val context: Context,
) : NotiSettings {
    companion object {
        private val Context.store: DataStore<Preferences> by preferencesDataStore("noti.settings")

        private val Theme = stringPreferencesKey("theme")
        private val DynamicColor = booleanPreferencesKey("dynamic_color")
        private val AMOLED = booleanPreferencesKey("amoled")
        private val Locale = intPreferencesKey("locale")
        private val GridCells = intPreferencesKey("gridCells")
        private val DescriptionMaxLines = intPreferencesKey("descriptionMaxLines")
    }

    override val theme: Settings<NotiTheme> = object : Settings<NotiTheme> {
        override val observe: Flow<NotiTheme>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    NotiTheme.valueOf(preferences[Theme] ?: NotiTheme.System.name)
                }

        override suspend fun update(newState: NotiTheme) {
            super.update(newState)
            context.store.edit { preferences ->
                preferences[Theme] = newState.name
            }
        }
    }

    override val dynamicColor: Settings<Boolean> = object : Settings<Boolean> {
        override val observe: Flow<Boolean>
            get() = context.store.data
                .catchIO()
                .map { preferences -> preferences[DynamicColor] ?: true }

        override suspend fun update(newState: Boolean) {
            super.update(newState)
            context.store.edit { preferences ->
                preferences[DynamicColor] = newState
            }
        }
    }

    override val amoled: Settings<Boolean> = object : Settings<Boolean> {
        override val observe: Flow<Boolean>
            get() = context.store.data
                .catchIO()
                .map { preferences -> preferences[AMOLED] ?: false }

        override suspend fun update(newState: Boolean) {
            super.update(newState)
            context.store.edit { preferences ->
                preferences[AMOLED] = newState
            }
        }
    }

    @Suppress("DEPRECATION")
    override val locale: Settings<NotiLocale> = object : Settings<NotiLocale> {
        override val observe: Flow<NotiLocale>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        context.resources.configuration.locales[0].language
                    } else {
                        context.resources.configuration.locale.language
                    }

                    preferences[Locale]?.let { NotiLocale.from(it) } ?: NotiLocale.from(locale)
                }

        override suspend fun update(newState: NotiLocale) {
            super.update(newState)
            context.store.edit { preferences ->
                preferences[Locale] = newState.ordinal
            }
        }
    }

    override val gridCells: Settings<Int> = object : Settings<Int> {
        override val observe: Flow<Int>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    val cells = if (context.resources.getBoolean(R.bool.isTablet)) 3 else 1

                    preferences[GridCells] ?: cells
                }

        override suspend fun update(newState: Int) {
            super.update(newState)
            context.store.edit { preferences ->
                preferences[GridCells] = newState
            }
        }
    }

    override val descriptionMaxLines: Settings<Int> = object : Settings<Int> {
        override val observe: Flow<Int>
            get() = context.store.data
                .catchIO()
                .map { preferences -> preferences[DescriptionMaxLines] ?: 3 }

        override suspend fun update(newState: Int) {
            super.update(newState)
            context.store.edit { preferences ->
                preferences[DescriptionMaxLines] = newState
            }
        }
    }

    private fun Flow<Preferences>.catchIO(): Flow<Preferences> {
        return catch { e ->
            logcat { e.stackTraceToString() }

            if (e is IOException) emit(emptyPreferences())
            else throw e
        }
    }
}