package com.example.aplikasi_dicoding_event_first.utils.data

import com.google.gson.annotations.SerializedName

interface IEvent {
    val id: Int

    val name: String
    val summary: String

    val imageLogo: String
}