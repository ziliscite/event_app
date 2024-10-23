package com.example.aplikasi_dicoding_event_first.data.remote.retrofit

import com.example.aplikasi_dicoding_event_first.data.remote.response.EventsDetailResponse
import com.example.aplikasi_dicoding_event_first.data.remote.response.EventsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// The older ones is for the past submission, now deleted
interface ApiServiceNew {
    @GET("events")
    suspend fun getEvents(
        // Whether event is upcoming (1) or finished (0), all events (-1) / default
        @Query("active") active: Int? = null,
        // Search event name
        @Query("q") q: String = "",
        // Event list limit (default 40)
        @Query("limit") limit: Int? = null
    ): EventsResponse

    @GET("events/{id}")
    suspend fun getEventDetail(
        // Id is required, not optional
        @Path("id") id: Int
    ): EventsDetailResponse
}
