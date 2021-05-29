package com.example.timetracking.repository.local_data_source.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DateConverters {

    @TypeConverter
    fun saveAttachments(listOfString: List<String>): String {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun getAttachments(listOfString: String): List<String> {
        return Gson().fromJson(
            listOfString,
            object : TypeToken<List<Double?>?>() {}.type
        )
    }
}