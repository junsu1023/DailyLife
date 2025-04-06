package com.example.domain.repository

import com.example.domain.model.ClassificationModel

interface ClassificationRepository {
    suspend fun getClassificationList(): List<ClassificationModel>

    suspend fun addClassification(classification: ClassificationModel): Result<Unit>

    suspend fun deleteClassification(classification: ClassificationModel): Result<Unit>

    suspend fun updateClassification(classification: ClassificationModel): Result<Unit>
}