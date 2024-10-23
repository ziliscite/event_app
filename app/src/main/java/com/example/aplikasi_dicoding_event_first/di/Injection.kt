package com.example.aplikasi_dicoding_event_first.di

import android.content.Context
import com.example.aplikasi_dicoding_event_first.data.local.room.EventDatabase
import com.example.aplikasi_dicoding_event_first.data.local.manager.SettingsPreferences
import com.example.aplikasi_dicoding_event_first.data.local.manager.dataStore
import com.example.aplikasi_dicoding_event_first.data.remote.retrofit.ApiConfig
import com.example.aplikasi_dicoding_event_first.repository.EventRepository
import com.example.aplikasi_dicoding_event_first.repository.FavoriteEventRepository

object Injection {
    fun provideRepository(): EventRepository {
        val apiService = ApiConfig.getApiServiceNew()
        return EventRepository.getInstance(apiService)
    }

    fun provideFavoriteRepository(context: Context): FavoriteEventRepository {
        val database = EventDatabase.getInstance(context)
        val dao = database.favoriteEventDao()
        return FavoriteEventRepository.getInstance(dao)
    }

    fun providePreferences(context: Context): SettingsPreferences {
        return SettingsPreferences.getInstance(context.dataStore)
    }
}
