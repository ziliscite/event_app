package com.example.aplikasi_dicoding_event_first.data.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Since there is no state, I figured its okay to just make it a singleton
object ApiConfig {
    fun getApiService(): ApiService {
        // TODO("Delete Logging After The Project Is Done")
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            // I'm not gonna wait minutes just to get connection error
            .connectTimeout(15, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(15, TimeUnit.SECONDS)    // Read timeout
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://event-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
