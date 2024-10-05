package com.example.aplikasi_dicoding_event_first.utils.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasi_dicoding_event_first.EventsListAdapter
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem

class EventsListDelegate(
    private val recyclerView: RecyclerView,
    private val layoutManager: RecyclerView.LayoutManager,
    private val adapter: EventsListAdapter
) {
    fun setupRecyclerView() {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    fun updateData(newData: List<ListEventsItem>) {
        adapter.submitList(newData)
        recyclerView.adapter = adapter
    }
}