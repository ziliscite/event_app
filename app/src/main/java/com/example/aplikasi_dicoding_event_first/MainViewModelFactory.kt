package com.example.aplikasi_dicoding_event_first

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasi_dicoding_event_first.data.manager.SettingsPreferences
import com.example.aplikasi_dicoding_event_first.di.Injection

class MainViewModelFactory private constructor(
    private val preferences: SettingsPreferences
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: MainViewModelFactory? = null
        fun getInstance(context: Context): MainViewModelFactory {
            return instance ?: synchronized(this) {
                val preferences = Injection.providePreferences(context)
                instance ?: MainViewModelFactory(preferences)
            }.also {
                instance = it
            }
        }
    }
}