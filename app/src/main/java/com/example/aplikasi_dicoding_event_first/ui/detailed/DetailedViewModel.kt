package com.example.aplikasi_dicoding_event_first.ui.detailed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.response.Event
import com.example.aplikasi_dicoding_event_first.utils.network.EventsFetcher
import com.example.aplikasi_dicoding_event_first.utils.network.EventsResponseHandler
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorPageDelegate
import com.example.aplikasi_dicoding_event_first.utils.ui.LoadingStateDelegate
import kotlinx.coroutines.launch

class DetailedViewModel(
    private val eventId: Int
) : ViewModel() {
    private val eventsFetcher: EventsFetcher = EventsFetcher
    private val eventsHandler: EventsResponseHandler = EventsResponseHandler

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _event

    val loadingState: LoadingStateDelegate = LoadingStateDelegate()
    val errorState: ErrorPageDelegate = ErrorPageDelegate()

    fun getEvent() { viewModelScope.launch {
        if (_event.value != null && errorState.error.value?.first == false) {
            return@launch
        }

        loadingState.wrapRequest {
            val eventDetail = eventsFetcher.fetchEventDetail(eventId, logTag = TAG)
            eventsHandler.getEventDetailHandler(eventDetail, errorState) {
                it?.let {
                    _event.value = it
                }
            }
        }
    }}

    companion object{
        private const val TAG = "FinishedViewModel"
    }
}
