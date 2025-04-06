package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entitiy.ClassificationEntity

@Dao
interface ClassificationDao {
    @Query("select * from classification_db")
    fun getClassificationList(): List<ClassificationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addClassification(classification: ClassificationEntity)

    @Delete
    fun deleteClassification(classification: ClassificationEntity)

    @Update
    fun updateClassification(classification: ClassificationEntity)
}