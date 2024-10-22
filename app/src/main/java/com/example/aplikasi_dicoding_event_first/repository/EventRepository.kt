package com.example.aplikasi_dicoding_event_first.repository

import android.util.Log
import com.example.aplikasi_dicoding_event_first.data.remote.response.Event
import com.example.aplikasi_dicoding_event_first.data.remote.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.data.remote.retrofit.ApiServiceNew
import com.example.aplikasi_dicoding_event_first.utils.network.EventResult
import java.io.IOException

class EventRepository(
    private val apiService: ApiServiceNew
) {
    suspend fun getEvents(
        active: Int? = null,
        q: String = "",
        limit: Int? = null
    ): EventResult<List<ListEventsItem>> {
        return try {
            val response = apiService.getEvents(active, q, limit)
            if (response.error) {
                throw Exception(response.message)
            }

            val events = response.listEvents
            if (events.isEmpty()) {
                throw Exception("No events found")
            }

            EventResult.Success(events)
        } catch (e: IOException) {
            Log.e("EventRepository", "onFailure: ${e.message.toString()}")
            EventResult.Error("Internet connection problem")
        } catch (e: Exception) {
            Log.e("EventRepository", "onFailure: ${e.message.toString()}")
            EventResult.Error(e.message.toString())
        }
    }

    suspend fun getEventDetail(
        eventId: Int
    ): EventResult<Event> {
        return try {
            val response = apiService.getEventDetail(eventId)
            if (response.error) {
                throw Exception(response.message)
            }

            val event = response.event
            EventResult.Success(event)
        } catch (e: IOException) {
            Log.e("EventRepository", "onFailure: ${e.message.toString()}")
            EventResult.Error("Internet connection problem")
        } catch (e: Exception) {
            Log.e("EventRepository", "onFailure: ${e.message.toString()}")
            EventResult.Error(e.message.toString())
        }
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiServiceNew
        ): EventRepository {
            return instance ?: synchronized(this) {
                instance ?: EventRepository(apiService)
            }.also {
                instance = it
            }
        }
    }
}
