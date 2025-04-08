package com.example.data.datasource

import com.example.data.dao.CardCompanyDao
import com.example.data.entitiy.CardCompanyEntity

class CardCompanyDataSource(
    private val cardCompanyDao: CardCompanyDao
) {
    fun getClassificationList(): List<CardCompanyEntity> = cardCompanyDao.getCardCompanyList()

    fun addClassification(cardCompanyEntity: CardCompanyEntity) = cardCompanyDao.addCardCompany(cardCompanyEntity)

    fun deleteClassification(cardCompanyEntity: CardCompanyEntity) = cardCompanyDao.deleteCardCompany(cardCompanyEntity)

    fun updateClassification(cardCompanyEntity: CardCompanyEntity) = cardCompanyDao.updateCardCompany(cardCompanyEntity)
}