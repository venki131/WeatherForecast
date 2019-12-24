package com.venkatesh.forecast.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.venkatesh.forecast.data.network.response.CurrentWeatherResponse
import com.venkatesh.forecast.utils.UrlConstant
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val API_KEY = "a3c63fc22742d8cfa9c53ebb3777c773"

//http://api.weatherstack.com/current?access_key=a3c63fc22742d8cfa9c53ebb3777c773&query=New%20York&lang=en

interface ApixuWeatherApi {

    @GET(UrlConstant.CURRENT)
    fun getCurrentWeather(
        @Query("query") location: String,
        @Query("lang") langCode: String = "en",
        @Query("units") metrics: String = "m"
    ): Deferred<CurrentWeatherResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): ApixuWeatherApi {
            val requestInterceptor = Interceptor {

                val url = it.request()
                    .url
                    .newBuilder()
                    .addQueryParameter("access_key",
                        API_KEY
                    )
                    .build()

                val request = it.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor it.proceed(request)
            }
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(UrlConstant.BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApixuWeatherApi::class.java)
        }
    }
}