package com.example.aplikasi_dicoding_event_first.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasi_dicoding_event_first.data.manager.SettingsPreferences
import com.example.aplikasi_dicoding_event_first.di.Injection

class SettingsViewModelFactory private constructor(
    private val preferences: SettingsPreferences
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: SettingsViewModelFactory? = null
        fun getInstance(context: Context): SettingsViewModelFactory {
            return instance ?: synchronized(this) {
                val preferences = Injection.providePreferences(context)
                instance ?: SettingsViewModelFactory(preferences)
            }.also {
                instance = it
            }
        }
    }
}