package com.example.aplikasi_dicoding_event_first.utils.ui

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasi_dicoding_event_first.EventsListAdapter
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem

class RecyclerViewDelegate(
    private val recyclerView: RecyclerView,
    private val layoutManager: RecyclerView.LayoutManager,
    private val adapter: EventsListAdapter
) {
    fun setup() {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    fun update(newData: List<ListEventsItem>) {
        adapter.submitList(newData)
        recyclerView.adapter = adapter
    }

    fun setPosition(position: Int) {
        layoutManager.scrollToPosition(position)
    }

    fun getPosition(): Int {
        val layout = layoutManager as LinearLayoutManager
        return layout.findFirstVisibleItemPosition()
    }
}
