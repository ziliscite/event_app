package com.example.aplikasi_dicoding_event_first.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasi_dicoding_event_first.di.Injection
import com.example.aplikasi_dicoding_event_first.repository.FavoriteEventRepository

class FavoriteViewModelFactory private constructor(
    private val favoriteEventRepository: FavoriteEventRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(favoriteEventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: FavoriteViewModelFactory? = null
        fun getInstance(context: Context): FavoriteViewModelFactory {
            return instance ?: synchronized(this) {
                val repository = Injection.provideFavoriteRepository(context)
                instance ?: FavoriteViewModelFactory(repository)
            }.also {
                instance = it
            }
        }
    }
}
