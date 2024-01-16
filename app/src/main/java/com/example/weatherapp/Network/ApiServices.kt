package com.example.weatherapp.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServices {

    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    val endpoints: ApiEndpoints
        get() {
            val retrofit: Retrofit by lazy {
                Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit.create(ApiEndpoints::class.java)
        }
}