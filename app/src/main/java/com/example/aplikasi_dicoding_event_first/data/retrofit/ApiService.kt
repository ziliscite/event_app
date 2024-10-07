package com.example.aplikasi_dicoding_event_first.data.retrofit

import com.example.aplikasi_dicoding_event_first.data.response.EventsDetailResponse
import com.example.aplikasi_dicoding_event_first.data.response.EventsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response

interface ApiService {
    @GET("events")
    suspend fun getEvents(
        // Whether event is upcoming (1) or finished (0), all events (-1) / default
        @Query("active") active: Int? = null,
        // Search event name
        @Query("q") q: String = "",
        // Event list limit (default 40)
        @Query("limit") limit: Int? = null
    ): Response<EventsResponse>

    @GET("events/{id}")
    suspend fun getEventDetail(
        // Id is required, not optional
        @Path("id") id: Int
    ): EventsDetailResponse
}
