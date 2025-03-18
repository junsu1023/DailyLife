package com.example.data.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.example.data.entitiy.TodoEntity
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
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

    @TypeConverter
    fun bitmapToByteArray(value: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        value.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        return outputStream.toByteArray()
    }

    @TypeConverter
    fun byteArrayToBitmap(value: ByteArray): Bitmap =
        BitmapFactory.decodeByteArray(value, 0, value.size)
}