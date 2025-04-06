package com.example.data.datasource

import com.example.data.dao.ClassificationDao
import com.example.data.entitiy.ClassificationEntity

class ClassificationDataSource(
    private val classificationDao: ClassificationDao
) {
    fun getClassificationList(): List<ClassificationEntity> = classificationDao.getClassificationList()

    fun addClassification(classification: ClassificationEntity) = classificationDao.addClassification(classification)

    fun deleteClassification(classification: ClassificationEntity) = classificationDao.deleteClassification(classification)

    fun updateClassification(classification: ClassificationEntity) = classificationDao.updateClassification(classification)
}