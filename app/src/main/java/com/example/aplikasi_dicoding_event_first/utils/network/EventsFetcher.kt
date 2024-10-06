package com.example.aplikasi_dicoding_event_first.utils.network

import android.util.Log
import com.example.aplikasi_dicoding_event_first.data.retrofit.ApiConfig
import com.example.aplikasi_dicoding_event_first.data.response.Event
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// No state is held, so an object it is
object EventsFetcher {
    suspend fun fetchEvents(
        active: Int? = null,
        search: String = "",
        limit: Int? = null,
        logTag: String
    ): List<ListEventsItem>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiConfig
                    .getApiService()
                    .getEvents(active, search, limit)

                if (response.error) {
                    throw Exception(response.message)
                }

                response.listEvents
            } catch (e: Exception) {
                Log.e(logTag, "onFailure: ${e.message}")
                null
            }
        }
    }

    suspend fun fetchEventDetail(
        id: Int,
        logTag: String
    ) : Event? {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiConfig
                    .getApiService()
                    .getEventDetail(id)

                if (response.error) {
                    throw Exception(response.message)
                }

                response.event
            } catch (e: Exception) {
                Log.e(logTag, "onFailure: ${e.message}")
                null
            }
        }
    }
}
