package com.example.data.repository

import com.example.data.datasource.ClassificationDataSource
import com.example.data.mapper.convertEntity
import com.example.data.mapper.convertModel
import com.example.domain.model.ClassificationModel
import com.example.domain.repository.ClassificationRepository
import com.example.domain.state.FailedState

class ClassificationRepositoryImpl(
    private val classificationDataSource: ClassificationDataSource
): ClassificationRepository {
    override suspend fun getClassificationList(): List<ClassificationModel> = classificationDataSource.getClassificationList().map { it.convertModel() }

    override suspend fun addClassification(classification: ClassificationModel): Result<Unit> = try {
        classificationDataSource.addClassification(classification.convertEntity())
        Result.success(Unit)
    } catch (t: FailedState) {
        Result.failure(FailedState.FailedAdd)
    }

    override suspend fun deleteClassification(classification: ClassificationModel): Result<Unit> = try {
        classificationDataSource.deleteClassification(classification.convertEntity())
        Result.success(Unit)
    } catch (t: FailedState) {
        Result.failure(FailedState.FailedDelete)
    }

    override suspend fun updateClassification(classification: ClassificationModel): Result<Unit> = try {
        classificationDataSource.updateClassification(classification.convertEntity())
        Result.success(Unit)
    } catch (t: FailedState) {
        Result.failure(FailedState.FailedUpdate)
    }
}