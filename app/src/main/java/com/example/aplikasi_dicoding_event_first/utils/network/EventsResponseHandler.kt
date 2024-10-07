package com.example.aplikasi_dicoding_event_first.utils.network

import com.example.aplikasi_dicoding_event_first.data.response.EventsResponse
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorPageDelegate

object EventsResponseHandler {
    fun getEventsHandler(response: Result<EventsResponse?>, errorState: ErrorPageDelegate, callback: (List<ListEventsItem>) -> Unit) {
        if (response.isSuccess) {
            response.getOrNull()?.listEvents?.let {
                if (it.isEmpty()) {
                    callback(it)
                    errorState.setError(true, "Oops, no results found!")
                } else {
                    callback(it)
                    errorState.setError(false, "")
                }
            }
        } else {
            response.exceptionOrNull()?.let {
                errorState.setError(true, it.message.orEmpty())
            }?: run {
                errorState.setError(true, "Oops, no results found!")
            }
        }
    }
}
