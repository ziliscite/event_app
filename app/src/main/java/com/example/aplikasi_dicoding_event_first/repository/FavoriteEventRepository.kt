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
    ): LiveData<EventResult<FavoriteEventEntity>> = liveData {
        emit(EventResult.Loading)
        try {
            val event = favoriteEventDao.getFavoriteEventById(eventId)
            val localData: LiveData<EventResult<FavoriteEventEntity>> = event.map {
                EventResult.Success(it)
            }
            emitSource(localData)
        } catch (e: Exception) {
            Log.d("EventRepository", "getEventDetail: ${e.message.toString()} ")
            emit(EventResult.Error(e.message.toString()))
        }
        emit(EventResult.Error("Unforeseen Error"))
    }

    suspend fun insertFavoriteEvent(
        event: FavoriteEventEntity
    ) {
        favoriteEventDao.insertFavoriteEvent(event)
    }

    suspend fun deleteFavoriteEvent(
        event: FavoriteEventEntity
    ) {
        favoriteEventDao.deleteFavoriteEvent(event.eventId)
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
