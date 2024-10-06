package com.example.aplikasi_dicoding_event_first.utils.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LoadingStateDelegate {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun wrapRequest(callback: suspend () -> Unit) {
        _isLoading.value = true
        callback()
        _isLoading.value = false
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}