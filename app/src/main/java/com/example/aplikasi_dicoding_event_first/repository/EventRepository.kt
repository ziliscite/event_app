package com.example.aplikasi_dicoding_event_first.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.aplikasi_dicoding_event_first.data.local.entity.FavoriteEventEntity
import com.example.aplikasi_dicoding_event_first.data.local.room.FavoriteEventDao
import com.example.aplikasi_dicoding_event_first.data.remote.retrofit.ApiService
import com.example.aplikasi_dicoding_event_first.data.remote.retrofit.ApiServiceNew
import com.example.aplikasi_dicoding_event_first.utils.network.EventResult

class EventRepository(
    private val apiService: ApiServiceNew,
    private val favoriteEventDao: FavoriteEventDao
) {
    suspend fun getEvents(
        active: Int? = null,
        q: String = "",
        limit: Int? = null
    ): EventResult<List<FavoriteEventEntity>> {
        return try {
            EventResult.Loading

            val response = apiService.getEvents(active, q, limit)
            val events = response.listEvents

            val listEvents = events.map {
                val isFavorite = favoriteEventDao.isEventFavorite(it.id)
                FavoriteEventEntity(
                    eventId = it.id,
                    name = it.name,
                    category = it.category,
                    summary = it.summary,
                    imageLogo = it.imageLogo,
                    link = it.link,
                    description = it.description,
                    ownerName = it.ownerName,
                    cityName = it.cityName,
                    mediaCover = it.mediaCover,
                    registrants = it.registrants,
                    quota = it.quota,
                    beginTime = it.beginTime,
                    endTime = it.endTime
                )
            }

            EventResult.Success(listEvents)
        } catch (e: Exception) {
            Log.d("EventRepository", "getEvents: ${e.message.toString()} ")
            EventResult.Error(e.message.toString())
        }
    }

    fun getEventDetail(
        eventId: Int
    ): LiveData<EventResult<FavoriteEventEntity>> = liveData {
        emit(EventResult.Loading)
        try {
            val response = apiService.getEventDetail(eventId)
            val event = response.event.let {
                val isFavorite = favoriteEventDao.isEventFavorite(it.id)
                FavoriteEventEntity(
                    eventId = it.id,
                    name = it.name,
                    category = it.category,
                    summary = it.summary,
                    imageLogo = it.imageLogo,
                    link = it.link,
                    description = it.description,
                    ownerName = it.ownerName,
                    cityName = it.cityName,
                    mediaCover = it.mediaCover,
                    registrants = it.registrants,
                    quota = it.quota,
                    beginTime = it.beginTime,
                    endTime = it.endTime
                )
            }
            emit(EventResult.Success(event))
        } catch (e: Exception) {
            Log.d("EventRepository", "getEventDetail: ${e.message.toString()} ")
            emit(EventResult.Error(e.message.toString()))
        }
        emit(EventResult.Error("Unforeseen Error"))
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiServiceNew,
            favoriteEventDao: FavoriteEventDao,
        ): EventRepository {
            return instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, favoriteEventDao)
            }.also {
                instance = it
            }
        }
    }
}
