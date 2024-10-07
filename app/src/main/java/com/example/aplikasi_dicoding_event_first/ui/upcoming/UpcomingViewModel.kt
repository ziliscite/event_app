package com.example.aplikasi_dicoding_event_first.ui.upcoming

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

class UpcomingViewModel : ViewModel() {
    private val eventsFetcher: EventsFetcher = EventsFetcher
    private val eventsHandler: EventsResponseHandler = EventsResponseHandler

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    val loadingState: LoadingStateDelegate = LoadingStateDelegate()

    val errorState: ErrorPageDelegate = ErrorPageDelegate()

    fun getEvents(query: String = "") { viewModelScope.launch {
        if (!_events.value.isNullOrEmpty()) {
            loadingState.setLoading(false)
            return@launch
        }

        // Less repetitions? Idk, just trying my best
        loadingState.wrapRequest {
            val response = eventsFetcher.fetchEvents(1, search = query, logTag = TAG)
            eventsHandler.getEventsHandler(response, errorState) {
                _events.value = it
            }
        }
    }}

    val scrollState: ScrollStateDelegate = ScrollStateDelegate()

    companion object{
        const val TAG = "UpcomingViewModel"
    }
}
