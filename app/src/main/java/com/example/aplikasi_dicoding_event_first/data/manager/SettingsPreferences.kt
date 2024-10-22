package com.example.aplikasi_dicoding_event_first.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsPreferences private constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val themeKey = booleanPreferencesKey("theme_setting")
    private val reminderKey = booleanPreferencesKey("reminder_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map {
            it[themeKey] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit {
            it[themeKey] = isDarkModeActive
        }
    }

    fun getReminderSetting(): Flow<Boolean> {
        return dataStore.data.map {
            it[reminderKey] ?: false
        }
    }

    suspend fun saveReminderSetting(isReminderActive: Boolean) {
        dataStore.edit {
            it[reminderKey] = isReminderActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingsPreferences? = null
        fun getInstance(
            dataStore: DataStore<Preferences>
        ): SettingsPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}