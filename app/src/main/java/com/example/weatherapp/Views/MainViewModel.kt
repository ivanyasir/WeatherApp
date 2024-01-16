package com.example.weatherapp.Views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.Model.Weathers
import com.example.weatherapp.Network.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _weathers = MutableLiveData<Weathers>()
    val weathers: LiveData<Weathers> = _weathers

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getWeathers(lat: Double, lon: Double, appID: String, lang: String, units: String){
        _isLoading.value = true
        val getMovies = ApiServices.endpoints.getWeathers(lat, lon, appID, lang, units)
        getMovies.enqueue(object : Callback<Weathers> {
            override fun onResponse(call: Call<Weathers>, response: Response<Weathers>) {
                _isLoading.value = false

                if (response.isSuccessful){
                    _weathers.value = response.body()
                } else {
                    _message.value = response.code().toString()
                    _message.value = response.message().toString()
                }
            }

            override fun onFailure(call: Call<Weathers>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message
            }

        })
    }

}