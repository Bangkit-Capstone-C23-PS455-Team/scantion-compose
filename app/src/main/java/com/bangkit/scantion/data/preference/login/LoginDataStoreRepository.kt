package com.bangkit.scantion.data.preference.login

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.scantion.model.UserLog
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_login_data")

class LoginDataStoreRepository(context: Context) {
    private object PreferencesKey {
        val userLogKey = stringPreferencesKey(name = "user_log")
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveLoginState(userLog: UserLog) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.userLogKey] = Gson().toJson(userLog)
        }
    }

    fun readLoginState(): Flow<UserLog?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val userLogJson = preferences[PreferencesKey.userLogKey]
                userLogJson?.let { Gson().fromJson(it, UserLog::class.java) }
            }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKey.userLogKey)
        }
    }
}