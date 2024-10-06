package com.example.aplikasi_dicoding_event_first.ui.detailed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.response.Event
import com.example.aplikasi_dicoding_event_first.utils.network.EventsFetcher
import kotlinx.coroutines.launch

class DetailedViewModel(
    private val eventId: Int
) : ViewModel() {
    private val eventsFetcher: EventsFetcher = EventsFetcher

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _event

    fun getEvent() { viewModelScope.launch {
        eventsFetcher.fetchEventDetail(eventId, logTag = TAG)?.let {
            _event.value = it
        }
    }}

    companion object{
        private const val TAG = "FinishedViewModel"
    }
}