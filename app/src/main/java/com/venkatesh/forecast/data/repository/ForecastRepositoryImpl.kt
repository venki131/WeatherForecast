package com.venkatesh.forecast.data.repository

import androidx.lifecycle.LiveData
import com.venkatesh.forecast.data.db.CurrentWeatherDao
import com.venkatesh.forecast.data.db.WeatherLocationDao
import com.venkatesh.forecast.data.db.entity.CurrentWeatherEntry
import com.venkatesh.forecast.data.db.entity.WeatherLocation
import com.venkatesh.forecast.data.network.WeatherNetworkDataSource
import com.venkatesh.forecast.data.network.response.CurrentWeatherResponse
import com.venkatesh.forecast.data.provider.LocationProvider
import com.venkatesh.forecast.data.provider.UnitProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

const val IMPERIAL = "IMPERIAL"

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val unitProvider: UnitProvider,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever {
            persistFetchedCurrentWeather(it)
        }
    }

    override suspend fun getWeather(metric: Boolean): LiveData<CurrentWeatherEntry> {
        initWeatherData()
        return withContext(Dispatchers.IO) {
            return@withContext currentWeatherDao.getWeather()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocation().value

        if (lastWeatherLocation == null
            || locationProvider.hasLocationChanged(lastWeatherLocation)
        ) {
            fetchCurrentWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language,
            getMetricSystem()
        )
    }

    private fun getMetricSystem(): String {
        return if (unitProvider.getUnitSystem().name == IMPERIAL)
            "F"
        else
            "M"
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}