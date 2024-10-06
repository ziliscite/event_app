package com.example.aplikasi_dicoding_event_first.ui.finished

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.utils.network.EventsFetcher
import com.example.aplikasi_dicoding_event_first.utils.ui.LoadingStateDelegate
import com.example.aplikasi_dicoding_event_first.utils.ui.ScrollStateDelegate
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


class FinishedViewModel : ViewModel() {
    private val eventsFetcher: EventsFetcher = EventsFetcher

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getEvents() { viewModelScope.launch {
        _isLoading.value = true
        eventsFetcher.fetchEvents(0, logTag = TAG)?.let {
            _events.value = it
        }
        _isLoading.value = false
    }}

    val scrollState: ScrollStateDelegate = ScrollStateDelegate()

    companion object {
        private const val TAG = "FinishedViewModel"
    }
}
