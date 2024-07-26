package com.example.spinwheel2.Model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromItemsList(items: List<Items>): String {
        val gson = Gson()
        return gson.toJson(items)
    }

    @TypeConverter
    fun toItemsList(data: String): List<Items> {
        val gson = Gson()
        val listType = object : TypeToken<List<Items>>() {}.type
        return gson.fromJson(data, listType)
    }
}
