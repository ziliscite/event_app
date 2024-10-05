package com.example.aplikasi_dicoding_event_first.utils.network

import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem

interface IEventsFetcher {
    fun fetchEvents(active: Int, logTag: String, callback: (List<ListEventsItem>?) -> Unit)
}
