package com.example.aplikasi_dicoding_event_first.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.manager.SettingsPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferences: SettingsPreferences
) : ViewModel() {
    private var _themeSetting: MutableLiveData<Boolean> = MutableLiveData()
    val themeSetting: LiveData<Boolean> get() = _themeSetting

    private var _reminderSetting: MutableLiveData<Boolean> = MutableLiveData()
    val reminderSetting: LiveData<Boolean> get() = _reminderSetting

    fun getThemeSettings() { viewModelScope.launch {
        preferences.getThemeSetting().collect{
            _themeSetting.postValue(it)
        }
    }}

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