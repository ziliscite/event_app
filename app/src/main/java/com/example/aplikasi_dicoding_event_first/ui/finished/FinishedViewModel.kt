package com.example.aplikasi_dicoding_event_first.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.utils.network.EventsFetcher
import com.example.aplikasi_dicoding_event_first.utils.ui.LoadingStateDelegate
import com.example.aplikasi_dicoding_event_first.utils.ui.ScrollStateDelegate
import kotlinx.coroutines.launch

class FinishedViewModel : ViewModel() {
    private val eventsFetcher: EventsFetcher = EventsFetcher

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    val loadingState: LoadingStateDelegate = LoadingStateDelegate()

    fun getEvents() { viewModelScope.launch {
        // Kinda sick of seeing the brief loading scene when the data is instantly loaded...
        if (!_events.value.isNullOrEmpty()) {
            loadingState.setLoading(false)
            return@launch
        }

        // Less repetitions? Idk, just trying my best
        loadingState.wrapRequest {
            eventsFetcher.fetchEvents(0, logTag = TAG)?.let {
                _events.value = it
            } ?: run {
                // To later handle if there is an error
                // Like, showing a *data not available* screen
            }
        }
    }}

    val scrollState: ScrollStateDelegate = ScrollStateDelegate()

    companion object {
        private const val TAG = "FinishedViewModel"
    }
}
