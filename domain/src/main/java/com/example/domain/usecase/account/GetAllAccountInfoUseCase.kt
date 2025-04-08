package com.example.domain.usecase.account

import com.example.domain.model.AccountModel
import com.example.domain.repository.AccountRepository
import javax.inject.Inject

class GetAllAccountInfoUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): List<AccountModel> = accountRepository.getAllAccountInfo()
}