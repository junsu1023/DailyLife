package com.example.data.mapper

import com.example.data.entitiy.AccountEntity
import com.example.domain.model.AccountModel

fun AccountEntity.convertAccountItemModel(): AccountModel =
    AccountModel(
        this.id,
        this.kind,
        this.date,
        this.cost,
        this.classification,
        this.content
    )

fun AccountModel.convertAccountItemEntity(): AccountEntity =
    AccountEntity(
        this.id,
        this.kind,
        this.date,
        this.cost,
        this.classification,
        this.content
    )