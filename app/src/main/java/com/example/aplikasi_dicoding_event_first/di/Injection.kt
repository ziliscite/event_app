package com.example.aplikasi_dicoding_event_first.di

import android.content.Context
import com.example.aplikasi_dicoding_event_first.data.local.room.EventDatabase
import com.example.aplikasi_dicoding_event_first.data.manager.SettingsPreferences
import com.example.aplikasi_dicoding_event_first.data.manager.dataStore
import com.example.aplikasi_dicoding_event_first.data.remote.retrofit.ApiConfig
import com.example.aplikasi_dicoding_event_first.repository.EventRepository

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiServiceNew()
        val database = EventDatabase.getInstance(context)
        val dao = database.favoriteEventDao()
        return EventRepository.getInstance(apiService, dao)
    }

    fun providePreferences(context: Context): SettingsPreferences {
        return SettingsPreferences.getInstance(context.dataStore)
    }
}