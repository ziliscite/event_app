package com.example.aplikasi_dicoding_event_first.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.manager.SettingsPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferences: SettingsPreferences
) : ViewModel() {
    private var _reminderSetting: MutableLiveData<Boolean> = MutableLiveData()
    val reminderSetting: LiveData<Boolean> get() = _reminderSetting

    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) { viewModelScope.launch {
        preferences.saveThemeSetting(isDarkModeActive)
    }}

    fun getReminderSettings() { viewModelScope.launch {
        preferences.getReminderSetting().collect{
            _reminderSetting.postValue(it)
        }
    }}

    fun saveReminderSetting(isReminderActive: Boolean) { viewModelScope.launch {
        preferences.saveReminderSetting(isReminderActive)
    }}
}