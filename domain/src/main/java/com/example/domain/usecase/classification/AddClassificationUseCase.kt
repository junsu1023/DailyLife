package com.example.domain.usecase.classification

import com.example.domain.model.ClassificationModel
import com.example.domain.repository.ClassificationRepository
import com.example.domain.state.FailedState
import javax.inject.Inject

class AddClassificationUseCase @Inject constructor(
    private val classificationRepository: ClassificationRepository
) {
    suspend operator fun invoke(classification: ClassificationModel): Result<Unit> =
        try {
            classificationRepository.addClassification(classification)
            Result.success(Unit)
        } catch (e: FailedState) {
            Result.failure(FailedState.FailedAdd)
        }
}