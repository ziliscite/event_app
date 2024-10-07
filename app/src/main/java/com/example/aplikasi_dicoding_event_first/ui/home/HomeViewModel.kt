package com.example.aplikasi_dicoding_event_first.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.utils.network.EventsFetcher
import com.example.aplikasi_dicoding_event_first.utils.network.EventsResponseHandler
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorPageDelegate
import com.example.aplikasi_dicoding_event_first.utils.ui.LoadingStateDelegate
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val eventsFetcher: EventsFetcher = EventsFetcher
    private val eventsHandler: EventsResponseHandler = EventsResponseHandler

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> get() = _finishedEvents

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> get() = _upcomingEvents

    val finishedLoadingState: LoadingStateDelegate = LoadingStateDelegate()
    val upcomingLoadingState: LoadingStateDelegate = LoadingStateDelegate()

    val errorState: ErrorPageDelegate = ErrorPageDelegate()

    fun getFinishedEvents() { viewModelScope.launch {
        if (!_finishedEvents.value.isNullOrEmpty()) {
            finishedLoadingState.setLoading(false)
            return@launch
        }

        finishedLoadingState.wrapRequest {
            val response = eventsFetcher.fetchEvents(0, limit = 5, logTag = TAG)
            eventsHandler.getEventsHandler(response, errorState) {
                _finishedEvents.value = it
            }
        }
    }}

    fun getUpcomingEvents() { viewModelScope.launch {
        if (!_upcomingEvents.value.isNullOrEmpty()) {
            upcomingLoadingState.setLoading(false)
            return@launch
        }

        upcomingLoadingState.wrapRequest {
            val response = eventsFetcher.fetchEvents(1, limit = 5, logTag = TAG)
            eventsHandler.getEventsHandler(response, errorState) {
                _upcomingEvents.value = it
            }
        }
    }}

    companion object {
        private const val TAG = "FinishedViewModel"
    }
}
