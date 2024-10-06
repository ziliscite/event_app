package com.example.aplikasi_dicoding_event_first.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.utils.network.EventsFetcher
import com.example.aplikasi_dicoding_event_first.utils.ui.ScrollStateDelegate
import kotlinx.coroutines.launch

class UpcomingViewModel : ViewModel() {
    private val eventsFetcher: EventsFetcher = EventsFetcher

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getEvents() { viewModelScope.launch {
        _isLoading.value = true
        eventsFetcher.fetchEvents(1, logTag = TAG)?.let {
            _events.value = it
        }
        _isLoading.value = false
    }}

    val scrollState: ScrollStateDelegate = ScrollStateDelegate()

    companion object{
        const val TAG = "UpcomingViewModel"
    }
}