package com.example.domain.repository

import com.example.domain.model.CardCompanyModel

interface CardCompanyRepository {
    suspend fun getClassificationList(): List<CardCompanyModel>

    suspend fun addClassification(cardCompanyModel: CardCompanyModel): Result<Unit>

    suspend fun deleteClassification(cardCompanyModel: CardCompanyModel): Result<Unit>

    suspend fun updateClassification(cardCompanyModel: CardCompanyModel): Result<Unit>
}