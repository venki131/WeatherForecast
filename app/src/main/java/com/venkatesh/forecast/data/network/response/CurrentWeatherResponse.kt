package com.venkatesh.forecast.data.network.response

import com.google.gson.annotations.SerializedName
import com.venkatesh.forecast.data.db.entity.CurrentWeatherEntry
import com.venkatesh.forecast.data.db.entity.WeatherLocation
import com.venkatesh.forecast.data.db.entity.Request


data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocation,
    val request: Request
)