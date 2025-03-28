package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.dao.CalendarDao
import com.example.data.dao.TodoDao
import com.example.data.entitiy.TodoEntity

@Database(entities = [TodoEntity::class], version = 3)
@TypeConverters(com.example.data.converters.TypeConverters::class)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun calendarDao(): CalendarDao
}