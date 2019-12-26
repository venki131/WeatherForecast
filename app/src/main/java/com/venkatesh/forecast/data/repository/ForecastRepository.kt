package com.venkatesh.forecast.data.repository

import androidx.lifecycle.LiveData
import com.venkatesh.forecast.data.db.entity.CurrentWeatherEntry

interface ForecastRepository {
    suspend fun getWeather(metric: Boolean) : LiveData<CurrentWeatherEntry>
}