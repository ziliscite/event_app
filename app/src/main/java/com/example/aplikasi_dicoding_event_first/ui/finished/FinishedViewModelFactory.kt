package com.example.aplikasi_dicoding_event_first.ui.finished

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasi_dicoding_event_first.di.Injection
import com.example.aplikasi_dicoding_event_first.repository.EventRepository
class FinishedViewModelFactory private constructor(
    private val eventRepository: EventRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinishedViewModel::class.java)) {
            return FinishedViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: FinishedViewModelFactory? = null
        fun getInstance(): FinishedViewModelFactory {
            return instance ?: synchronized(this) {
                val repository = Injection.provideRepository()
                instance ?: FinishedViewModelFactory(repository)
            }.also {
                instance = it
            }
        }
    }
}