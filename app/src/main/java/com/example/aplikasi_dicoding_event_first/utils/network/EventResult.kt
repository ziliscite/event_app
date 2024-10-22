package com.example.aplikasi_dicoding_event_first.utils.network

sealed class EventResult<out R> private constructor() {
    data class Success<out T>(val data: T) : EventResult<T>()
    data class Error(val error: String) : EventResult<Nothing>()
    data object Loading : EventResult<Nothing>()
}
