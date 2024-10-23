package com.example.aplikasi_dicoding_event_first.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aplikasi_dicoding_event_first.utils.data.IEvent

@Entity(tableName = "event")
class FavoriteEventEntity(
    @field:ColumnInfo(name = "eventId")
    @field:PrimaryKey
    override val id: Int,

    @field:ColumnInfo("name")
    override val name: String,

    @field:ColumnInfo("summary")
    override val summary: String,

    @field:ColumnInfo("imageLogo")
    override val imageLogo: String,

    @field:ColumnInfo("category")
    val category: String,

    @field:ColumnInfo("link")
    val link: String,

    @field:ColumnInfo("description")
    val description: String,

    @field:ColumnInfo("ownerName")
    val ownerName: String,

    @field:ColumnInfo("cityName")
    val cityName: String,

    @field:ColumnInfo("mediaCover")
    val mediaCover: String,

    @field:ColumnInfo("registrants")
    val registrants: Int,

    @field:ColumnInfo("quota")
    val quota: Int,

    @field:ColumnInfo("beginTime")
    val beginTime: String,

    @field:ColumnInfo("endTime")
    val endTime: String,

//    @field:ColumnInfo(name = "isFavorite")
//    var isFavorite: Boolean,
) : IEvent
