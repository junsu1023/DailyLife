package com.example.data.mapper

import com.example.data.entitiy.ClassificationEntity
import com.example.domain.model.ClassificationModel

fun ClassificationModel.convertEntity(): ClassificationEntity =
    ClassificationEntity(
        this.id,
        this.classification
    )

fun ClassificationEntity.convertModel(): ClassificationModel =
    ClassificationModel(
        this.id,
        this.classification
    )