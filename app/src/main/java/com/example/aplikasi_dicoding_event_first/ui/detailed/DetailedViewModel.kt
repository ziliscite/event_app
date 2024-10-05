package com.example.aplikasi_dicoding_event_first.ui.detailed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasi_dicoding_event_first.data.response.ApiConfig
import com.example.aplikasi_dicoding_event_first.data.response.Event
import com.example.aplikasi_dicoding_event_first.data.response.EventsDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailedViewModel(
    private val eventId: Int
) : ViewModel() {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _event

    fun getEvent() {
        val client = ApiConfig.getApiService().getEventDetail(eventId)
        client.enqueue(object : Callback<EventsDetailResponse> {
            override fun onResponse(
                call: Call<EventsDetailResponse>,
                response: Response<EventsDetailResponse>
            ) {
                if (!response.isSuccessful) {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }

                val responseBody = response.body()
                if (responseBody != null) {
                    _event.value = responseBody.event
                }
            }

            override fun onFailure(
                call: Call<EventsDetailResponse>, t: Throwable
            ) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = "FinishedViewModel"
    }
}