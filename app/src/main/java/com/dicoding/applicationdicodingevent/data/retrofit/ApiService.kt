package com.dicoding.applicationdicodingevent.data.retrofit

import com.dicoding.applicationdicodingevent.data.response.DetailEventResponse
import com.dicoding.applicationdicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int
    ):Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent (@Path("id") id: String) : Call<DetailEventResponse>

    @GET("events")
    fun getEventsBySearch(
        @Query("active") active: Int,
        @Query("q") q: String
    ):Call <EventResponse>
}