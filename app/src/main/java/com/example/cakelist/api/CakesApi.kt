package com.example.cakelist.api

import com.example.cakelist.models.Cake
import retrofit2.Response
import retrofit2.http.GET

interface CakesApi {
    @GET("/waracle_cake-android-client")
    suspend fun getQuotes(): Response<List<Cake>>
}