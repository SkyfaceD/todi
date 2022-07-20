package org.skyfaced.todi.settings

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import logcat.logcat
import java.io.IOException

class TodiSettingsImpl(
    private val context: Context
) : TodiSettings {
    companion object {
        private val Context.store: DataStore<Preferences> by preferencesDataStore("todi.settings")

        private val Theme = stringPreferencesKey("theme")
        private val DynamicColor = booleanPreferencesKey("dynamic_color")
        private val AMOLED = booleanPreferencesKey("amoled")
        private val Locale = intPreferencesKey("locale")
    }

    override val theme: Settings<TodiTheme> = object : Settings<TodiTheme> {
        override val observe: Flow<TodiTheme>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    TodiTheme.valueOf(preferences[Theme] ?: TodiTheme.System.name)
                }

        override suspend fun update(newState: TodiTheme) {
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
                .map { preferences -> preferences[AMOLED] ?: true }

        override suspend fun update(newState: Boolean) {
            super.update(newState)
            context.store.edit { preferences ->
                preferences[AMOLED] = newState
            }
        }
    }

    @Suppress("DEPRECATION")
    override val locale: Settings<TodiLocale> = object : Settings<TodiLocale> {
        override val observe: Flow<TodiLocale>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    val language = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        context.resources.configuration.locales[0].language
                    } else {
                        context.resources.configuration.locale.language
                    }

                    preferences[Locale]?.let { TodiLocale.from(it) } ?: TodiLocale.from(language)
                }

        override suspend fun update(newState: TodiLocale) {
            super.update(newState)
            context.store.edit { prefs ->
                prefs[Locale] = newState.ordinal
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