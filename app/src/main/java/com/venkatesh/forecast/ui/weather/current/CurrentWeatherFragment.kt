package com.venkatesh.forecast.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.venkatesh.forecast.R
import com.venkatesh.forecast.data.network.ApixuWeatherApi
import com.venkatesh.forecast.data.network.ConnectivityInterceptorImpl
import com.venkatesh.forecast.data.network.WeatherNetworkDataSourceImpl
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CurrentWeatherFragment : Fragment() {

    companion object {
        fun newInstance() =
            CurrentWeatherFragment()
    }

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CurrentWeatherViewModel::class.java)
        // TODO: Use the ViewModel

        var apiService = ApixuWeatherApi(ConnectivityInterceptorImpl(this.context!!))

        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)

        weatherNetworkDataSource.downloadedCurrentWeather.observe(this, Observer {
            textView.text = it.toString()
        })

        GlobalScope.launch(Dispatchers.Main) {
            weatherNetworkDataSource.fetchCurrentWeather("Bangalore", "en", "m")
            //val currentWeatherResponse = apiService.getCurrentWeather("Bangalore", "", "m").await()
        }
    }

}
