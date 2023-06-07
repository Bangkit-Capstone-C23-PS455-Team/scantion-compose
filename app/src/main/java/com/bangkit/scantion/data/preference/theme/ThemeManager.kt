package com.bangkit.scantion.data.preference.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_prefs")

class ThemeManager(context: Context) {

    private object PreferencesKey {
        val darkModeKey = booleanPreferencesKey("theme_mode")
        val initModeKey = booleanPreferencesKey("initial_theme")
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveDarkTheme(isNightMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.darkModeKey] = isNightMode
        }
    }

    suspend fun setInitTheme(isInitMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.initModeKey] = isInitMode
        }
    }

    fun getDarkMode(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val darkMode = preferences[PreferencesKey.darkModeKey] ?: false
                darkMode
            }
    }

    fun getInitTheme(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val initTheme = preferences[PreferencesKey.initModeKey] ?: true
                initTheme
            }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKey.darkModeKey)
            preferences.remove(PreferencesKey.initModeKey)
        }
    }
}