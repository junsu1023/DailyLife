package com.example.data.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_company_db")
data class CardCompanyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val cardCompany: String
)
