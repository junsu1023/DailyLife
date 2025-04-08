package com.example.domain.usecase.cardcompany

import com.example.domain.model.CardCompanyModel
import com.example.domain.repository.CardCompanyRepository
import javax.inject.Inject

class GetCardCompanyListUseCase @Inject constructor(
    private val cardCompanyRepository: CardCompanyRepository
) {
    suspend operator fun invoke(): List<CardCompanyModel> = cardCompanyRepository.getClassificationList()
}