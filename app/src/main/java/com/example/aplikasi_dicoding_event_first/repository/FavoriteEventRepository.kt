package com.example.aplikasi_dicoding_event_first.repository

import androidx.lifecycle.LiveData
import com.example.aplikasi_dicoding_event_first.data.local.entity.FavoriteEventEntity
import com.example.aplikasi_dicoding_event_first.data.local.room.FavoriteEventDao

class FavoriteEventRepository(
    private val favoriteEventDao: FavoriteEventDao
) {
    fun getFavoriteEvents(): LiveData<List<FavoriteEventEntity>> {
        return favoriteEventDao.getFavoriteEvents()
    }

    fun getFavoriteEventDetail(eventId: Int): LiveData<FavoriteEventEntity> {
        return favoriteEventDao.getFavoriteEventById(eventId)
    }

    suspend fun insertFavoriteEvent(event: FavoriteEventEntity) {
        favoriteEventDao.insertFavoriteEvent(event)
    }

    suspend fun deleteFavoriteEvent(eventId: Int) {
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
