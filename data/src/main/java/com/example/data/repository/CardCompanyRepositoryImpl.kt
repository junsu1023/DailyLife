package com.example.data.repository

import com.example.data.datasource.CardCompanyDataSource
import com.example.data.mapper.convertEntity
import com.example.data.mapper.convertModel
import com.example.domain.model.CardCompanyModel
import com.example.domain.repository.CardCompanyRepository
import com.example.domain.state.FailedState
import javax.inject.Inject

class CardCompanyRepositoryImpl @Inject constructor(
    private val cardCompanyDataSource: CardCompanyDataSource
): CardCompanyRepository {
    override suspend fun getClassificationList(): List<CardCompanyModel> = cardCompanyDataSource.getClassificationList().map { it.convertModel() }

    override suspend fun addClassification(cardCompanyModel: CardCompanyModel): Result<Unit> = try {
        cardCompanyDataSource.addClassification(cardCompanyModel.convertEntity())
        Result.success(Unit)
    } catch (t: FailedState) {
        Result.failure(FailedState.FailedAdd)
    }

    override suspend fun deleteClassification(cardCompanyModel: CardCompanyModel): Result<Unit> = try {
        cardCompanyDataSource.deleteClassification(cardCompanyModel.convertEntity())
        Result.success(Unit)
    } catch (t: FailedState) {
        Result.failure(FailedState.FailedDelete)
    }

    override suspend fun updateClassification(cardCompanyModel: CardCompanyModel): Result<Unit> = try {
        cardCompanyDataSource.updateClassification(cardCompanyModel.convertEntity())
        Result.success(Unit)
    } catch (t: FailedState) {
        Result.failure(FailedState.FailedUpdate)
    }
}