package com.example.aplikasi_dicoding_event_first.utils.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ScrollStateDelegate {
    private val _scrollPosition = MutableLiveData<Int>()
    val position: LiveData<Int> get() = _scrollPosition

    fun savePosition(position: Int) {
        _scrollPosition.value = position
    }
}
