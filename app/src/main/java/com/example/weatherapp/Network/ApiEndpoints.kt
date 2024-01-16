package com.example.weatherapp.Network

import com.example.weatherapp.Model.Weathers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndpoints {

    @GET("weather")
    fun getWeathers(@Query("lat") lat: Double,
                    @Query("lon") lon: Double,
                    @Query("appid") appid: String,
                    @Query("lang") lang: String,
                    @Query("units") units: String,
                    ): Call<Weathers>
}