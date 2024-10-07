package com.example.aplikasi_dicoding_event_first.utils.network

import android.util.Log
import com.example.aplikasi_dicoding_event_first.data.retrofit.ApiConfig
import com.example.aplikasi_dicoding_event_first.data.response.Event
import com.example.aplikasi_dicoding_event_first.data.response.EventsResponse
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

// No state is held, so an object it is
object EventsFetcher {
    suspend fun fetchEvents(
        active: Int? = null,
        search: String = "",
        limit: Int? = null,
        logTag: String
    ): Result<EventsResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiConfig
                    .getApiService()
                    .getEvents(active, search, limit)
                if (response.isSuccessful) {
                    Result.success(response.body())
                } else {
                    Result.failure(Exception("${response.code()} ${response.message()}"))
                }
            } catch (e: IOException) { // When fetching data too long / no internet connection
                Log.e(logTag, "onFailure: ${e.message}")
                Result.failure(Exception("Internet connection problem"))
            } catch (e: Exception) {
                Log.e(logTag, "onFailure: ${e.message}")
                Result.failure(Exception("Unexpected error!"))
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
