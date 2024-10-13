package com.dicoding.applicationdicodingevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.applicationdicodingevent.data.response.EventResponse
import com.dicoding.applicationdicodingevent.data.response.ListEventsItem
import com.dicoding.applicationdicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    companion object {
        private const val EVENT_ID = 0
        private const val EVENT_ID_SEARCH = -1
    }

    init {
        findEvent()
    }

    private fun findEvent() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(EVENT_ID)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _events.value = response.body()?.listEvents
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Failure: ${t.message.toString()}"
            }
        })
    }

    fun filterEvents(query: String) {
        if (query.isEmpty()) {
            findEvent()
        } else {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getEventsBySearch(EVENT_ID_SEARCH, query)
            client.enqueue(object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _events.value = response.body()?.listEvents
                        _errorMessage.value = null
                    } else {
                        _errorMessage.value = "Error: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Failure: ${t.message.toString()}"
                }
            })
        }
    }
}
