package com.example.data.converters

import android.graphics.Color
import androidx.room.TypeConverter
import com.example.data.entitiy.TodoEntity
import com.google.gson.Gson
import java.util.Date

class TypeConverters {
    @TypeConverter
    fun dateToLong(value: Date?) = value?.time

    @TypeConverter
    fun longToDate(value: Long?) = value?.let { Date(it) }

    @TypeConverter
    fun listToJson(value: List<TodoEntity>): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String): List<TodoEntity> = Gson().fromJson(value, Array<TodoEntity>::class.java).toList()
}