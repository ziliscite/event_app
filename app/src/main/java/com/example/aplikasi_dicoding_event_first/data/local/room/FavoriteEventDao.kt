package com.example.aplikasi_dicoding_event_first.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aplikasi_dicoding_event_first.data.local.entity.FavoriteEventEntity

@Dao
interface FavoriteEventDao {
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    fun getFavoriteEventById(eventId: Int): LiveData<FavoriteEventEntity>

    @Query("SELECT * FROM event WHERE isFavorite = 1")
    fun getFavoriteEvents(): LiveData<List<FavoriteEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteEvent(event: FavoriteEventEntity)

    @Query("DELETE FROM event WHERE eventId = :eventId")
    suspend fun deleteFavoriteEvent(eventId: Int)

    @Query("SELECT EXISTS(SELECT * FROM event WHERE eventId = :eventId AND isFavorite = 1)")
    suspend fun isEventFavorite(eventId: Int): Boolean
}