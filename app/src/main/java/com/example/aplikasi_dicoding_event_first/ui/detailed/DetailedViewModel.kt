package com.example.aplikasi_dicoding_event_first.ui.detailed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_dicoding_event_first.data.local.entity.FavoriteEventEntity
import com.example.aplikasi_dicoding_event_first.data.remote.response.Event
import com.example.aplikasi_dicoding_event_first.repository.EventRepository
import com.example.aplikasi_dicoding_event_first.repository.FavoriteEventRepository
import com.example.aplikasi_dicoding_event_first.utils.data.FavoriteEventDetailDTO
import com.example.aplikasi_dicoding_event_first.utils.network.EventResult
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorPageDelegate
import kotlinx.coroutines.launch

class DetailedViewModel(
    private val eventRepository: EventRepository,
    private val favoriteRepository: FavoriteEventRepository
) : ViewModel() {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _event

    private val _detailEvent = MutableLiveData<EventResult<FavoriteEventDetailDTO>>()
    val detailEvent: LiveData<EventResult<FavoriteEventDetailDTO>> get() = _detailEvent

    val errorState: ErrorPageDelegate = ErrorPageDelegate()

    fun getEventDetailFavorite(eventId: Int): LiveData<FavoriteEventEntity> {
        return favoriteRepository.getFavoriteEventDetail(eventId)
    }

    fun getEventDetail(eventId: Int) { viewModelScope.launch {
        if (detailEvent.value.let { it is EventResult.Success }) {
            return@launch
        }

        when (val response = eventRepository.getEventDetail(eventId)) {
            is EventResult.Success -> {
                errorState.setError(false, "")
                _detailEvent.postValue(EventResult.Success(
                    FavoriteEventDetailDTO(event = response.data, isFavorite = false)
                ))
            }

            is EventResult.Error -> {
                errorState.setError(true, response.error)
                _detailEvent.postValue(EventResult.Error(response.error))
            }

            is EventResult.Loading -> {
                errorState.setError(false, "")
                _detailEvent.postValue(EventResult.Loading)
            }
        }
    }}

    fun insertFavorite(event: Event) { viewModelScope.launch {
        val eventEntity = event.run {
            FavoriteEventEntity(
                id = id,
                summary = summary,
                mediaCover = mediaCover,
                name = name,
                category = category,
                imageLogo = imageLogo,
                link = link,
                description = description,
                ownerName = ownerName,
                cityName = cityName,
                registrants = registrants,
                quota = quota,
                beginTime = beginTime,
                endTime = endTime,
            )
        }

        Log.d("INSERT", "insertFavorite: $eventEntity")
        favoriteRepository.insertFavoriteEvent(eventEntity)
    }}

    fun deleteFavorite(eventId: Int) { viewModelScope.launch {
        favoriteRepository.deleteFavoriteEvent(eventId)
    }}
}
