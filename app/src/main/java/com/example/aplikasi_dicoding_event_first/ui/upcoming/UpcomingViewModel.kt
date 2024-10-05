package com.example.aplikasi_dicoding_event_first.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.utils.network.EventsFetcher
import com.example.aplikasi_dicoding_event_first.utils.network.IEventsFetcher

class UpcomingViewModel : ViewModel() {
    private val eventsFetcher: EventsFetcher = EventsFetcher()

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    fun getEvents() {
        eventsFetcher.fetchEvents(1, TAG) {
            _events.value = it
        }
    }

    companion object{
        private const val TAG = "UpcomingViewModel"
    }
}