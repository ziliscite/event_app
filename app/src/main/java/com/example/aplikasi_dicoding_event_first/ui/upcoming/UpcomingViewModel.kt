package com.example.aplikasi_dicoding_event_first.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.remote.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.repository.EventRepository
import com.example.aplikasi_dicoding_event_first.utils.network.EventResult
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorPageDelegate
import com.example.aplikasi_dicoding_event_first.utils.ui.ScrollStateDelegate
import kotlinx.coroutines.launch

class UpcomingViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _upcomingEvents = MutableLiveData<EventResult<List<ListEventsItem>>>()
    val upcomingEvents: LiveData<EventResult<List<ListEventsItem>>> get() = _upcomingEvents

    val scrollState: ScrollStateDelegate = ScrollStateDelegate()

    val errorState: ErrorPageDelegate = ErrorPageDelegate()

    fun getUpcomingEvents() { viewModelScope.launch {
        if (upcomingEvents.value.let { it is EventResult.Success }) {
            return@launch
        }

        _upcomingEvents.postValue(EventResult.Loading)
        when (val response = eventRepository.getEvents(1)) {
            is EventResult.Success -> {
                errorState.setError(false, "")
                _upcomingEvents.postValue(EventResult.Success(response.data))
            }

            is EventResult.Error -> {
                errorState.setError(true, response.error)
                _upcomingEvents.postValue(EventResult.Error(response.error))
            }

            is EventResult.Loading -> {
                errorState.setError(false, "")
                _upcomingEvents.postValue(EventResult.Loading)
            }
        }
    }}
}
