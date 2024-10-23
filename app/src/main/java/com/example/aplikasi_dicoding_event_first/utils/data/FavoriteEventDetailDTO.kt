package com.example.aplikasi_dicoding_event_first.utils.data

import com.example.aplikasi_dicoding_event_first.data.remote.response.Event

data class FavoriteEventDetailDTO(
    val event: Event,
    val isFavorite: Boolean,
)
