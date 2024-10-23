package com.example.aplikasi_dicoding_event_first.ui.finished

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

class FinishedViewModel(
    private val eventsRepository: EventRepository
) : ViewModel() {
    val errorState: ErrorPageDelegate = ErrorPageDelegate()

    private val _finishedEvents = MutableLiveData<EventResult<List<ListEventsItem>>>()
    val finishedEvents: LiveData<EventResult<List<ListEventsItem>>> get() = _finishedEvents

    val scrollState: ScrollStateDelegate = ScrollStateDelegate()

    fun searchFinishedEvents(query: String) { viewModelScope.launch {
        _finishedEvents.postValue(EventResult.Loading)
        when (val response = eventsRepository.getEvents(0, query)) {
            is EventResult.Success -> {
                errorState.setError(false, "")
                _finishedEvents.postValue(EventResult.Success(response.data))
            }

            is EventResult.Error -> {
                errorState.setError(true, response.error)
                _finishedEvents.postValue(EventResult.Error(response.error))
            }

            is EventResult.Loading -> {
                errorState.setError(false, "")
                _finishedEvents.postValue(EventResult.Loading)
            }
        }
    }}

    fun getFinishedEvents() {
        if (finishedEvents.value.let { it is EventResult.Success }) {
            return
        }

        searchFinishedEvents("")
    }
}
