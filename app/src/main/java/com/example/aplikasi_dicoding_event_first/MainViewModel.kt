package com.example.aplikasi_dicoding_event_first

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.aplikasi_dicoding_event_first.data.manager.SettingsPreferences

class MainViewModel(
    private val preferences: SettingsPreferences
) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }
}
