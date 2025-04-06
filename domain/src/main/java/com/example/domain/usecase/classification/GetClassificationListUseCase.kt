package com.example.domain.usecase.classification

import com.example.domain.model.ClassificationModel
import com.example.domain.repository.ClassificationRepository
import javax.inject.Inject

class GetClassificationListUseCase @Inject constructor(
    private val classificationRepository: ClassificationRepository
) {
    suspend operator fun invoke(): List<ClassificationModel> = classificationRepository.getClassificationList()
}