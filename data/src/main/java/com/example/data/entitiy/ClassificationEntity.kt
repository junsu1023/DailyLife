package com.example.data.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classification_db")
data class ClassificationEntity(
    @PrimaryKey(autoGenerate = false) val id: Long? = null,
    val classification: String
)
