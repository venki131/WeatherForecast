package com.venkatesh.forecast.utils

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun StringsListToJson(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}