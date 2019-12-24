package com.venkatesh.forecast.data.repository

import androidx.lifecycle.LiveData
import com.venkatesh.forecast.data.db.CurrentWeatherDao
import com.venkatesh.forecast.data.db.entity.CurrentWeatherEntry
import com.venkatesh.forecast.data.network.WeatherNetworkDataSource
import com.venkatesh.forecast.data.network.response.CurrentWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever {
            persistFetchedCurrentWeather(it)
        }
    }

    override suspend fun getWeather(): LiveData<CurrentWeatherEntry> {
        initWeatherData()
        return withContext(Dispatchers.IO) {
            return@withContext currentWeatherDao.getWeather()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
        }
    }

    private suspend fun initWeatherData() {
        if (isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            "Bangalore",
            Locale.getDefault().language,
            getMetricSystem()
        )
    }

    private fun getMetricSystem(): String {
        val countryCode: String = Locale.getDefault().country
        if ("US" == countryCode ||
            "LR" == countryCode ||
            "MM" == countryCode
        )
            return "F"
        else
            return "M"
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}