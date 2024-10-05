package com.example.aplikasi_dicoding_event_first.data.response

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(
        // Whether event is upcoming (1) or finished (0), all events (-1) / default
        @Query("active") active: Int? = null,
        // Search event name
        @Query("q") q: String = "",
        // Event list limit (default 40)
        @Query("limit") limit: Int? = null
    ): Call<EventsResponse>

    @GET("events/{id}")
    fun getEventDetail(
        // Id is required, not optional
        @Path("id") id: Int
    ): Call<EventsDetailResponse>
}
