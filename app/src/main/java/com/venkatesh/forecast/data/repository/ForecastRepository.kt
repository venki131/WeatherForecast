package com.venkatesh.forecast.data.repository

import androidx.lifecycle.LiveData
import com.venkatesh.forecast.data.db.entity.CurrentWeatherEntry
import com.venkatesh.forecast.data.db.entity.WeatherLocation

interface ForecastRepository {
    suspend fun getWeather(metric: Boolean): LiveData<CurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}