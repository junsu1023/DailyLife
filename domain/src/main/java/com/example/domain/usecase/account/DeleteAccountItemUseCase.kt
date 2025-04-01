package com.example.domain.usecase.account

import com.example.domain.model.AccountModel
import com.example.domain.repository.AccountRepository
import com.example.domain.state.FailedState
import javax.inject.Inject

class DeleteAccountItemUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(accountItemModel: AccountModel): Result<Unit> = try {
        accountRepository.deleteAccountItemEntity(accountItemModel)
        Result.success(Unit)
    } catch (t: Throwable) {
        Result.failure(FailedState.FailedDelete)
    }
}