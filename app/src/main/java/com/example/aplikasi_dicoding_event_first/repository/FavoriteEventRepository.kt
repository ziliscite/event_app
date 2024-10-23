package com.example.aplikasi_dicoding_event_first.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.aplikasi_dicoding_event_first.data.local.entity.FavoriteEventEntity
import com.example.aplikasi_dicoding_event_first.data.local.room.FavoriteEventDao
import com.example.aplikasi_dicoding_event_first.utils.network.EventResult

class FavoriteEventRepository(
    private val favoriteEventDao: FavoriteEventDao
) {
    fun getFavoriteEvents(): LiveData<List<FavoriteEventEntity>> {
        return favoriteEventDao.getFavoriteEvents()
    }

    fun getFavoriteEventDetail(
        eventId: Int
    ): LiveData<FavoriteEventEntity> {
        return favoriteEventDao.getFavoriteEventById(eventId)
    }

    suspend fun insertFavoriteEvent(event: FavoriteEventEntity) {
//        Log.d("EventRepository", "getEventDetail: ${event.name} ")
        favoriteEventDao.insertFavoriteEvent(event)
    }

    suspend fun deleteFavoriteEvent(eventId: Int) {
//        Log.d("EventRepository", "getEventDetail: $eventId ")
        favoriteEventDao.deleteFavoriteEvent(eventId)
    }

    companion object {
        @Volatile
        private var instance: FavoriteEventRepository? = null
        fun getInstance(
            favoriteEventDao: FavoriteEventDao,
        ): FavoriteEventRepository {
            return instance ?: synchronized(this) {
                instance ?: FavoriteEventRepository(favoriteEventDao)
            }.also {
                instance = it
            }
        }
    }
}
