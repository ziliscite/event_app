package com.example.aplikasi_dicoding_event_first.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasi_dicoding_event_first.di.Injection
import com.example.aplikasi_dicoding_event_first.repository.EventRepository

class HomeViewModelFactory private constructor(
    private val eventRepository: EventRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HomeViewModelFactory? = null
        fun getInstance(): HomeViewModelFactory {
            return instance ?: synchronized(this) {
                val repository = Injection.provideRepository()
                instance ?: HomeViewModelFactory(repository)
            }.also {
                instance = it
            }
        }
    }
}
