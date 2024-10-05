package com.example.aplikasi_dicoding_event_first.ui.detailed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/*
    Since passing data directly into View Model might not be a good idea,
    a little birdie told me to crate a factory to then create the detail View Model object.
*/
class DetailedViewModelFactory(private val eventId: Int) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailedViewModel::class.java)) {
            return DetailedViewModel(eventId) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
