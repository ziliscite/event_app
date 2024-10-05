package com.example.aplikasi_dicoding_event_first.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.utils.network.EventsFetcher


class FinishedViewModel : ViewModel() {
    private val eventsFetcher: EventsFetcher = EventsFetcher()

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    fun getEvents() {
        // It's less repetitions now ain't it
        // This, however, got me reconsidering of doing another abstraction on top of upcoming/finished fragment
        eventsFetcher.fetchEvents(0, TAG) {
            _events.value = it
        }
    }

    companion object {
        private const val TAG = "FinishedViewModel"
    }
}
