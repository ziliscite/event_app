package com.example.aplikasi_dicoding_event_first.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.ui.finished.FinishedViewModel
import com.example.aplikasi_dicoding_event_first.ui.upcoming.UpcomingViewModel
import com.example.aplikasi_dicoding_event_first.ui.upcoming.UpcomingViewModel.Companion
import com.example.aplikasi_dicoding_event_first.utils.network.EventsFetcher
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val eventsFetcher: EventsFetcher = EventsFetcher

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> get() = _finishedEvents

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> get() = _upcomingEvents

    fun getFinishedEvents() { viewModelScope.launch {
        _finishedEvents.value = eventsFetcher.fetchEvents(0, limit = 5, logTag = TAG)
    }}

    fun getUpcomingEvents() { viewModelScope.launch {
        _upcomingEvents.value = eventsFetcher.fetchEvents(1, limit = 5, logTag = TAG)
    }}

    companion object {
        private const val TAG = "FinishedViewModel"
    }
}