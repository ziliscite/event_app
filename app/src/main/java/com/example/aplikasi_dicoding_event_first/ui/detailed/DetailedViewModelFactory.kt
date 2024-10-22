package com.example.aplikasi_dicoding_event_first.ui.detailed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailedViewModelFactory(
    private val eventId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailedViewModel::class.java)) {
            return DetailedViewModel(eventId) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
