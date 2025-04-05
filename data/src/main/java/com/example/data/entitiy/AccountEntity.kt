package com.example.data.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_db")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val kind: String,
    val date: String,
    val cost: Long,
    val classification: String,
    val content: String
)