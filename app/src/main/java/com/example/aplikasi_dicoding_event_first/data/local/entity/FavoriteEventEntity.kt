package com.example.aplikasi_dicoding_event_first.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
class FavoriteEventEntity(
    @field:ColumnInfo(name = "eventId")
    @field:PrimaryKey
    val eventId: Int,

    @field:ColumnInfo("name")
    val name: String,

    @field:ColumnInfo("category")
    val category: String,

    @field:ColumnInfo("summary")
    val summary: String,

    @field:ColumnInfo("imageLogo")
    val imageLogo: String,

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
)