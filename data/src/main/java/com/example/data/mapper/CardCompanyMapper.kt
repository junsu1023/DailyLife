package com.example.data.mapper

import com.example.data.entitiy.CardCompanyEntity
import com.example.domain.model.CardCompanyModel

fun CardCompanyEntity.convertModel(): CardCompanyModel =
    CardCompanyModel(
        this.id,
        this.cardCompany
    )

fun CardCompanyModel.convertEntity(): CardCompanyEntity =
    CardCompanyEntity(
        this.id,
        this.cardCompany
    )