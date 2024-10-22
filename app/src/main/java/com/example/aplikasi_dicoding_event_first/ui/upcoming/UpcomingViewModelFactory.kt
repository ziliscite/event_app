package com.example.aplikasi_dicoding_event_first.ui.upcoming

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasi_dicoding_event_first.di.Injection
import com.example.aplikasi_dicoding_event_first.repository.EventRepository

class UpcomingViewModelFactory private constructor(
    private val eventRepository: EventRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
            return UpcomingViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: UpcomingViewModelFactory? = null
        fun getInstance(): UpcomingViewModelFactory {
            return instance ?: synchronized(this) {
                val repository = Injection.provideRepository()
                instance ?: UpcomingViewModelFactory(repository)
            }.also {
                instance = it
            }
        }
    }
}
