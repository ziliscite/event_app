package com.example.aplikasi_dicoding_event_first.utils.network

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.aplikasi_dicoding_event_first.data.response.ApiConfig
import com.example.aplikasi_dicoding_event_first.data.response.EventsResponse
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsFetcher : IEventsFetcher {
    override fun fetchEvents(
        active: Int,
        logTag: String,
        callback: (List<ListEventsItem>?) -> Unit
    ) {
        val client = ApiConfig.getApiService().getEvents(active)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                if (!response.isSuccessful) {
                    Log.e(logTag, "onFailure: ${response.message()}")
                    callback(null)
                    return
                }

                val responseBody = response.body()
                callback(responseBody?.listEvents)
            }

            override fun onFailure(
                call: Call<EventsResponse>, t: Throwable
            ) {
                Log.e(logTag, "onFailure: ${t.message}")
                callback(null)
            }
        })
    }
}