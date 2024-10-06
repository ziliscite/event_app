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

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError


    fun getEvents(query: String = "") { viewModelScope.launch {
        // Kinda sick of seeing the brief loading scene when the data is instantly loaded...
        if (!_events.value.isNullOrEmpty()) {
            loadingState.setLoading(false)
            return@launch
        }

        // Less repetitions? Idk, just trying my best
        loadingState.wrapRequest {
            // We might want to also return the message from events fetcher
            eventsFetcher.fetchEvents(0, search = query, logTag = TAG)?.let {
                _events.value = it
            } ?: run {
                _isError.value = true
                // To later handle if there is an error
                // Like, showing a *data not available* screen
            }
            // We do this after commit, justin case

//            val response = eventsFetcher.fetchEvents(0, search = query, logTag = TAG)
//            response?.listEvents?.let {
//                if (it.isEmpty()) {
//                    _isError.value = true
//                } else {
//                    _events.value = it
//                    _isError.value = false
//                }
//            } ?: run {
//                _isError.value = true
//            }
        }
    }}

    val scrollState: ScrollStateDelegate = ScrollStateDelegate()

    companion object {
        private const val TAG = "FinishedViewModel"
    }
}
