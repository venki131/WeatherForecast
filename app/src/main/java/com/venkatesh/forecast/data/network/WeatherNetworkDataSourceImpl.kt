package com.venkatesh.forecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.venkatesh.forecast.data.network.response.CurrentWeatherResponse
import com.venkatesh.forecast.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val apixuWeatherApi: ApixuWeatherApi
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()

    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String,
        metricSystem: String
    ) {
        try {
            val fetchCurrentWeather = apixuWeatherApi
                .getCurrentWeather(location, languageCode, metricSystem)
                .await()

            _downloadedCurrentWeather.postValue(fetchCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("connectivity", "no internet connection", e)
        }
    }
}