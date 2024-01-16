package com.example.weatherapp.Views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.weatherapp.Constants
import com.example.weatherapp.R
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: MainViewModel
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private var lat: Double by Delegates.notNull<Double>()
    private var lon: Double by Delegates.notNull<Double>()
    private lateinit var tvCity: TextView
    private lateinit var tvWeather: TextView
    private lateinit var tvtemperature: TextView
    private lateinit var tvWindSpeed: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvVisibility: TextView
    private lateinit var tvAirPressure: TextView
    private lateinit var imgIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init views xml
        progressBar = findViewById<ProgressBar>(R.id.progress_circular)
        tvCity = findViewById<TextView>(R.id.tv_city)
        tvWeather = findViewById<TextView>(R.id.tv_weather)
        tvtemperature = findViewById<TextView>(R.id.temperature)
        imgIcon = findViewById<ImageView>(R.id.img_icon)
        tvWindSpeed = findViewById<TextView>(R.id.tv_wind_speed)
        tvHumidity = findViewById<TextView>(R.id.tv_humidity)
        tvVisibility = findViewById<TextView>(R.id.tv_visibility)
        tvAirPressure = findViewById<TextView>(R.id.tv_AirPressure)

        // init viewmodel
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // get location
        getLocation()


        // loading state
        viewModel.isLoading.observe(this) {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        // log API message
        viewModel.message.observe(this) {
            Log.d("log", it)
        }

        // Api response and shows UI
        viewModel.weathers.observe(this) {
            Log.d("log", it.toString())
            tvCity.text = it.name
            tvtemperature.text = "${it.main!!.temp}\u2103"
            tvWeather.text = it.weather[0].main
            tvWindSpeed.text = it.wind!!.speed.toString()
            tvHumidity.text = it.main!!.humidity.toString()
            tvVisibility.text = it.visibility.toString()
            tvAirPressure.text = it.main!!.pressure.toString()

            // custom circular loading for https image
            val circularProgressDrawable = CircularProgressDrawable(this)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            // set https image into UI
            Glide.with(this)
                .load("https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png")
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(imgIcon)
        }
    }

    // function getting location
    private fun getLocation() {

        tvWeather.text = getString(R.string.memproses_data)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    // fun getting long lat coordinate
    override fun onLocationChanged(location: Location) {
        lat = location.latitude
        lon = location.longitude
        Log.d("log", "$lat")
        Log.d("log", "$lon")

        // getting information from API weather
        viewModel.getWeathers(lat, lon, Constants.appID, Constants.language, Constants.units)
    }

    // fun requesting permissions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("log", "Permission Granted")
            }
            else {
                Log.d("log", "Permission Denied")
            }
        }
    }
}