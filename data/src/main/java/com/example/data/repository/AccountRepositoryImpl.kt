package com.example.data.repository

import com.example.data.datasource.AccountDataSource
import com.example.data.mapper.convertAccountItemEntity
import com.example.data.mapper.convertAccountItemModel
import com.example.domain.model.AccountModel
import com.example.domain.repository.AccountRepository
import com.example.domain.state.FailedState

class AccountRepositoryImpl(
    private val accountDataSource: AccountDataSource
): AccountRepository {
    override suspend fun getAllAccountInfo(): List<AccountModel> =
        accountDataSource.getAllAccountInfo().map { it.convertAccountItemModel() }

    override suspend fun getCurrentYMAccountInfo(ym: String): List<AccountModel> =
        accountDataSource.getCurrentYMAccountInfo(ym).map { it.convertAccountItemModel() }

    override suspend fun addAccountItemEntity(accountItemModel: AccountModel): Result<Unit> = try {
        accountDataSource.addAccountItemEntity(accountItemModel.convertAccountItemEntity())
        Result.success(Unit)
    } catch (t: FailedState) {
        Result.failure(FailedState.FailedAdd)
    }

    override suspend fun deleteAccountItemEntity(accountItemModel: AccountModel): Result<Unit> = try {
        accountDataSource.deleteAccountItemEntity(accountItemModel.convertAccountItemEntity())
        Result.success(Unit)
    } catch (t: Throwable) {
        Result.failure(FailedState.FailedDelete)
    }

    override suspend fun updateAccountItemEntity(accountItemModel: AccountModel): Result<Unit> = try {
        accountDataSource.updateAccountItemEntity(accountItemModel.convertAccountItemEntity())
        Result.success(Unit)
    } catch (t: Throwable) {
        Result.failure(FailedState.FailedUpdate)
    }
}