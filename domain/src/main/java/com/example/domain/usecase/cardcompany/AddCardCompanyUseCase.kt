package com.example.domain.usecase.cardcompany

import com.example.domain.model.CardCompanyModel
import com.example.domain.repository.CardCompanyRepository
import com.example.domain.state.FailedState
import javax.inject.Inject

class AddCardCompanyUseCase @Inject constructor(
    private val cardCompanyRepository: CardCompanyRepository
) {
    suspend operator fun invoke(cardCompanyModel: CardCompanyModel): Result<Unit> = try {
        cardCompanyRepository.addClassification(cardCompanyModel)
        Result.success(Unit)
    } catch (t: FailedState) {
        Result.failure(FailedState.FailedAdd)
    }
}