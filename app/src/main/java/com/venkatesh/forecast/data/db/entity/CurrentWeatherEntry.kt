package com.venkatesh.forecast.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.venkatesh.forecast.utils.Converters

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeatherEntry(
    val cloudcover: Int,
    val feelslike: Double,
    val humidity: Int,
    @SerializedName("observation_time")
    val observationTime: String,
    val precip: Double,
    val pressure: Double,
    val temparature: Double,
    @SerializedName("uv_index")
    val uvIndex: Double,
    val visibility: Double,
    @SerializedName("weather_code")
    val weatherCode: Int,
    /*@SerializedName("weather_descriptions")
    @TypeConverters(Converters::class)
    val weatherDescriptions: List<String>,
    @SerializedName("weather_icons")
    @TypeConverters(Converters::class)
    val weatherIcons: List<String>,*/
    @SerializedName("wind_degree")
    val windDegree: Int,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_speed")
    val windSpeed: Double
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}