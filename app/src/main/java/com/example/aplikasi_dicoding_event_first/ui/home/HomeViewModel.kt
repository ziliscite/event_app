package com.example.aplikasi_dicoding_event_first.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.remote.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.repository.EventRepository
import com.example.aplikasi_dicoding_event_first.utils.network.EventResult
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorPageDelegate
import kotlinx.coroutines.launch

class HomeViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _finishedEvents = MutableLiveData<EventResult<List<ListEventsItem>>>()
    val finishedEvents: LiveData<EventResult<List<ListEventsItem>>> get() = _finishedEvents

    private val _upcomingEvents = MutableLiveData<EventResult<List<ListEventsItem>>>()
    val upcomingEvents: LiveData<EventResult<List<ListEventsItem>>> get() = _upcomingEvents

    val finishedErrorState: ErrorPageDelegate = ErrorPageDelegate()
    val upcomingErrorState: ErrorPageDelegate = ErrorPageDelegate()

    fun getUpcomingEvents() { viewModelScope.launch {
        if (upcomingEvents.value.let { it is EventResult.Success }) {
            return@launch
        }

        _upcomingEvents.postValue(EventResult.Loading)
        responseHandler(eventRepository.getEvents(1, limit = 5)) {
            _upcomingEvents.postValue(it)
        }
    }}

    fun getFinishedEvents() { viewModelScope.launch {
        if (finishedEvents.value.let { it is EventResult.Success }) {
            return@launch
        }

        _finishedEvents.postValue(EventResult.Loading)
        responseHandler(eventRepository.getEvents(0, limit = 5)) {
            _finishedEvents.postValue(it)
        }
    }}

    private fun responseHandler(
        response: EventResult<List<ListEventsItem>>,
        callback: (EventResult<List<ListEventsItem>>) -> Unit
    ) {
        when (response) {
            is EventResult.Success -> {
                finishedErrorState.setError(false, "")
                callback(EventResult.Success(response.data))
            }
            is EventResult.Error -> {
                finishedErrorState.setError(true, response.error)
                callback(EventResult.Error(response.error))
            }
            is EventResult.Loading -> {
                finishedErrorState.setError(false, "")
                callback(EventResult.Loading)
            }
        }
    }
}
