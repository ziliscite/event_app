package com.example.aplikasi_dicoding_event_first.ui.detailed

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasi_dicoding_event_first.di.Injection
import com.example.aplikasi_dicoding_event_first.repository.EventRepository
import com.example.aplikasi_dicoding_event_first.repository.FavoriteEventRepository

class DetailedViewModelFactory private constructor(
    private val eventRepository: EventRepository,
    private val favoriteRepository: FavoriteEventRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailedViewModel::class.java)) {
            return DetailedViewModel(eventRepository, favoriteRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: DetailedViewModelFactory? = null
        fun getInstance(context: Context): DetailedViewModelFactory {
            return instance ?: synchronized(this) {
                val repository = Injection.provideRepository()
                val favoriteRepository = Injection.provideFavoriteRepository(context)
                instance ?: DetailedViewModelFactory(repository, favoriteRepository)
            }.also {
                instance = it
            }
        }
    }
}
