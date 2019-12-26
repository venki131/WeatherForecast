package com.venkatesh.forecast.ui.weather.current

import androidx.lifecycle.ViewModel
import com.venkatesh.forecast.data.provider.UnitProvider
import com.venkatesh.forecast.data.repository.ForecastRepository
import com.venkatesh.forecast.internal.UnitSystem
import com.venkatesh.forecast.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    private val unitSystem = unitProvider.getUnitSystem() //get from settings

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getWeather(isMetric)
    }
}
