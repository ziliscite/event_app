package com.example.aplikasi_dicoding_event_first.utils.ui

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasi_dicoding_event_first.EventsListAdapter
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem

class RecyclerViewDelegate <in T: ListAdapter<ListEventsItem, *>> (
    private val recyclerView: RecyclerView,
    private val layoutManager: RecyclerView.LayoutManager,
    private val adapter: T
) {
    fun setup() {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter as ListAdapter<*, *>
    }

    fun update(newData: List<ListEventsItem>) {
        adapter.submitList(newData)
        recyclerView.adapter = adapter as ListAdapter<*, *>
    }

    fun setPosition(position: Int, offset: Int) {
        val layoutManager = layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(position, offset)
    }

    fun getPosition(): Pair<Int, Int> {
        val layoutManager = layoutManager as LinearLayoutManager

        val position = layoutManager.findFirstVisibleItemPosition()
        val view = layoutManager.findViewByPosition(position)

        val offset = view?.top ?: 0
        return Pair(position, offset)
    }
}
