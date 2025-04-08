package com.example.data.datasource

import com.example.data.dao.AccountDao
import com.example.data.entitiy.AccountEntity

class AccountDataSource(
    private val accountDao: AccountDao
) {
    fun getAllAccountInfo(): List<AccountEntity> = accountDao.getAllAccountInfo()

    fun getCurrentYMAccountInfo(ym: String): List<AccountEntity> = accountDao.getCurrentYMAccountInfo(ym)

    fun addAccountItemEntity(accountItemEntity: AccountEntity) = accountDao.addAccountItemEntity(accountItemEntity)

    fun deleteAccountItemEntity(accountItemEntity: AccountEntity) = accountDao.deleteAccountItemEntity(accountItemEntity)

    fun updateAccountItemEntity(accountItemEntity: AccountEntity) = accountDao.updateAccountItemEntity(accountItemEntity)
}