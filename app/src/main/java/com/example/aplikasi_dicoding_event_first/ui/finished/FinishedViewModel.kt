package com.example.aplikasi_dicoding_event_first.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.utils.network.EventsFetcher
import com.example.aplikasi_dicoding_event_first.utils.network.EventsResponseHandler
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorPageDelegate
import com.example.aplikasi_dicoding_event_first.utils.ui.LoadingStateDelegate
import com.example.aplikasi_dicoding_event_first.utils.ui.ScrollStateDelegate
import kotlinx.coroutines.launch

class FinishedViewModel : ViewModel() {
    private val eventsFetcher: EventsFetcher = EventsFetcher
    private val eventsHandler: EventsResponseHandler = EventsResponseHandler

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    val loadingState: LoadingStateDelegate = LoadingStateDelegate()

    val errorState: ErrorPageDelegate = ErrorPageDelegate()

    // Its like, imitation of event in the previous Dicoding exercise
    private val searchState = MutableLiveData(false)

    fun getEvents(query: String) { viewModelScope.launch {
        searchState.value = true
        loadingState.wrapRequest {
            val response = eventsFetcher.fetchEvents(0, search = query, logTag = TAG)
            eventsHandler.getEventsHandler(response, errorState) {
                _events.value = it
            }
        }
    }}

    fun initiateEvents() {
        if (_events.value != null && searchState.value == false) {
            return
        }

        getEvents("")
        searchState.value = false
    }

    fun onChange() {
        searchState.value = false
    }

    val scrollState: ScrollStateDelegate = ScrollStateDelegate()

    companion object {
        private const val TAG = "FinishedViewModel"
    }
}
