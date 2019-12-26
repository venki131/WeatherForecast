package com.venkatesh.forecast.ui.weather.current

import androidx.lifecycle.ViewModel
import com.venkatesh.forecast.data.repository.ForecastRepository
import com.venkatesh.forecast.internal.UnitSystem
import com.venkatesh.forecast.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    private val unitSystem = UnitSystem.METRIC //get from settings later

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getWeather(isMetric)
    }
}
