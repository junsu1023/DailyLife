package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entitiy.CardCompanyEntity

@Dao
interface CardCompanyDao {
    @Query("select * from card_company_db")
    fun getCardCompanyList(): List<CardCompanyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCardCompany(cardCompanyEntity: CardCompanyEntity)

    @Delete
    fun deleteCardCompany(cardCompanyEntity: CardCompanyEntity)

    @Update
    fun updateCardCompany(cardCompanyEntity: CardCompanyEntity)
}