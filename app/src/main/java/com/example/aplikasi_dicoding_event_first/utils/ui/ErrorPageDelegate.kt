package com.example.aplikasi_dicoding_event_first.utils.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

// Similar to scrollStateDelegate
class ErrorPageDelegate {
    private val _isError = MutableLiveData<Boolean>()
    private val _message = MutableLiveData<String>()

    private val _isErrorMessage: LiveData<Pair<Boolean, String>> by lazy {
        MediatorLiveData<Pair<Boolean, String>>().apply {
            addSource(_isError) { error ->
                val message = _message.value ?: ""
                value = Pair(error, message)
            }

            addSource(_message) { message ->
                val error = _isError.value ?: false
                value = Pair(error, message)
            }
        }
    }

    val error: LiveData<Pair<Boolean, String>> get() = _isErrorMessage

    fun setError(isError: Boolean, message: String) {
        _isError.value = isError
        _message.value = message
    }
}
