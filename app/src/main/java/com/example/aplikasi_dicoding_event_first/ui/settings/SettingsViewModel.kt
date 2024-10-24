package com.example.aplikasi_dicoding_event_first.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.local.manager.SettingsPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferences: SettingsPreferences
) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) { viewModelScope.launch {
        preferences.saveThemeSetting(isDarkModeActive)
    }}

    fun getReminderSettings(): LiveData<Boolean> {
        return preferences.getReminderSetting().asLiveData()
    }

    fun saveReminderSetting(isReminderActive: Boolean) { viewModelScope.launch {
        preferences.saveReminderSetting(isReminderActive)
    }}
}
