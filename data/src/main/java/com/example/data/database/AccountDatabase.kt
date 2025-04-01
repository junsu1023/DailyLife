package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.dao.AccountDao
import com.example.data.entitiy.AccountEntity

@Database(entities = [AccountEntity::class], version = 1)
@TypeConverters(com.example.data.converters.TypeConverters::class)
abstract class AccountDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao
}