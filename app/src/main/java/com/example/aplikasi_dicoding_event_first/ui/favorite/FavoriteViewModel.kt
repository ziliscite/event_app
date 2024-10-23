package com.example.aplikasi_dicoding_event_first.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasi_dicoding_event_first.data.local.entity.FavoriteEventEntity
import com.example.aplikasi_dicoding_event_first.repository.FavoriteEventRepository
import com.example.aplikasi_dicoding_event_first.utils.ui.ScrollStateDelegate

class FavoriteViewModel(
    private val favoriteEventRepository: FavoriteEventRepository
) : ViewModel() {
    val scrollState: ScrollStateDelegate = ScrollStateDelegate()

    // It can be shortened, yes, but I prefer it like this, kinda more aesthetic, hehe
    fun getFavoriteEvent(): LiveData<List<FavoriteEventEntity>> {
        return favoriteEventRepository.getFavoriteEvents()
    }
}
