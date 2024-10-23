package com.example.aplikasi_dicoding_event_first.data.remote.response

import com.example.aplikasi_dicoding_event_first.utils.data.IEvent
import com.google.gson.annotations.SerializedName

data class EventsResponse(
    @field:SerializedName("listEvents")
	val listEvents: List<ListEventsItem>,

    @field:SerializedName("error")
	val error: Boolean,

    @field:SerializedName("message")
	val message: String
)

data class ListEventsItem(
	@field:SerializedName("id")
	override val id: Int,

	@field:SerializedName("summary")
	override val summary: String,

	@field:SerializedName("name")
	override val name: String,

	@field:SerializedName("imageLogo")
	override val imageLogo: String,

	@field:SerializedName("registrants")
	val registrants: Int,

	@field:SerializedName("mediaCover")
	val mediaCover: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("ownerName")
	val ownerName: String,

	@field:SerializedName("cityName")
	val cityName: String,

	@field:SerializedName("quota")
	val quota: Int,

	@field:SerializedName("beginTime")
	val beginTime: String,

	@field:SerializedName("endTime")
	val endTime: String,

	@field:SerializedName("category")
	val category: String
) : IEvent
