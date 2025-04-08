package com.example.domain.repository

import com.example.domain.model.AccountModel

interface AccountRepository {
    suspend fun getAllAccountInfo(): List<AccountModel>

    suspend fun getCurrentYMAccountInfo(ym: String): List<AccountModel>

    suspend fun addAccountItemEntity(accountItemModel: AccountModel): Result<Unit>

    suspend fun deleteAccountItemEntity(accountItemModel: AccountModel): Result<Unit>

    suspend fun updateAccountItemEntity(accountItemModel: AccountModel): Result<Unit>
}