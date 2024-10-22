package com.example.aplikasi_dicoding_event_first.utils.network

import android.util.Log
import com.example.aplikasi_dicoding_event_first.data.remote.retrofit.ApiConfig
import com.example.aplikasi_dicoding_event_first.data.remote.response.EventsDetailResponse
import com.example.aplikasi_dicoding_event_first.data.remote.response.EventsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException

// No state is held, so an object it is
object EventsFetcher {
    suspend fun fetchEvents(
        active: Int? = null,
        search: String = "",
        limit: Int? = null,
        logTag: String
    ): Result<EventsResponse?> {
        // Not using call because it does not work with my loading state
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

            } catch (e: IOException) {
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
    ) : Result<EventsDetailResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiConfig
                    .getApiService()
                    .getEventDetail(id)

                if (response.isSuccessful) {
                    Result.success(response.body())
                } else {
                    Result.failure(Exception("${response.code()} ${response.message()}"))
                }

            } catch (e: IOException) {
                Log.e(logTag, "onFailure: ${e.message}")
                Result.failure(Exception("Internet connection problem"))
            } catch (e: Exception) {
                Log.e(logTag, "onFailure: ${e.message}")
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }
}
