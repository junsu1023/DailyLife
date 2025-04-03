package com.example.domain.usecase.account

import com.example.domain.model.AccountModel
import com.example.domain.repository.AccountRepository
import com.example.domain.state.AccountContinuationState
import com.example.domain.state.FailedState
import com.example.domain.util.ext.isAvailableDate
import com.example.domain.util.ext.isCostInRange
import javax.inject.Inject

class AddAccountItemUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(accountItemModel: AccountModel): Result<Unit> = try {
        when {
            !accountItemModel.date.isAvailableDate() -> Result.failure(AccountContinuationState.InValidDateFormat)
            !accountItemModel.cost.isCostInRange() ->Result.failure(AccountContinuationState.InvalidCostFormat)
            else -> {
                accountRepository.addAccountItemEntity(accountItemModel)
                Result.success(Unit)
            }
        }
    } catch (t: Throwable) {
        Result.failure(FailedState.FailedAdd)
    }
}