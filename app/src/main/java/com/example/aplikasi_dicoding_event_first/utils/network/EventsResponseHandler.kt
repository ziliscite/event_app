package com.example.aplikasi_dicoding_event_first.utils.network

import com.example.aplikasi_dicoding_event_first.data.remote.response.Event
import com.example.aplikasi_dicoding_event_first.data.remote.response.EventsDetailResponse
import com.example.aplikasi_dicoding_event_first.data.remote.response.EventsResponse
import com.example.aplikasi_dicoding_event_first.data.remote.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorPageDelegate

object EventsResponseHandler {
    private const val NORESULTERR = "Oops, no results found!"

    // Eah, doing this 4 times is quite a lot of repetitions, so, delegate it is
    fun getEventsHandler(response: Result<EventsResponse?>, errorState: ErrorPageDelegate, callback: (List<ListEventsItem>) -> Unit) {
        if (response.isSuccess) {
            response.getOrNull()?.listEvents?.let {
                if (it.isEmpty()) {
                    callback(it)
                    errorState.setError(true, NORESULTERR)
                } else {
                    callback(it)
                    errorState.setError(false, "")
                }
            }
        } else {
            response.exceptionOrNull()?.let {
                // Kinda to reset the viewmodel data so that.. uh.. I forgot, but it has something to do
                // with the error fragment overlapping
                callback(emptyList())
                errorState.setError(true, it.message.orEmpty())
            }?: run {
                callback(emptyList())
                errorState.setError(true, NORESULTERR)
            }
        }
    }
}
