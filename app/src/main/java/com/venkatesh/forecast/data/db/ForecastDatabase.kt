package com.venkatesh.forecast.data.db

import android.content.Context
import androidx.room.*
import com.venkatesh.forecast.data.db.entity.CurrentWeatherEntry
import com.venkatesh.forecast.utils.Converters

@Database(
    entities = [CurrentWeatherEntry::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao

    companion object {
        @Volatile
        private var instance: ForecastDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ForecastDatabase::class.java,
                "forecast.db"
            ).build()
    }
}