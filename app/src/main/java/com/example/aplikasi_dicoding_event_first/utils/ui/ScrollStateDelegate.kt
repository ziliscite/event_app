package com.example.aplikasi_dicoding_event_first.utils.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class ScrollStateDelegate {
    private val _scrollPosition = MutableLiveData<Int>()
    private val _scrollOffset = MutableLiveData<Int>()

    // Combined LiveData for position and offset
    private val _scrollPositionWithOffset: LiveData<Pair<Int, Int>> by lazy {
        MediatorLiveData<Pair<Int, Int>>().apply {
            addSource(_scrollPosition) { position ->
                val offset = _scrollOffset.value ?: 0
                value = Pair(position, offset)
            }

            addSource(_scrollOffset) { offset ->
                val position = _scrollPosition.value ?: 0
                value = Pair(position, offset)
            }
        }
    }

    val position: LiveData<Pair<Int, Int>> get() = _scrollPositionWithOffset

    fun savePosition(position: Int, offset: Int) {
        _scrollPosition.value = position
        _scrollOffset.value = offset
    }
}
