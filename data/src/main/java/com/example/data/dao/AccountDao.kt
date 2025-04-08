package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entitiy.AccountEntity

@Dao
interface AccountDao {
    @Query("select * from account_db order by date")
    fun getAllAccountInfo(): List<AccountEntity>

    @Query("select * from account_db where substr(date, 1, 7) == :ym order by date")
    fun getCurrentYMAccountInfo(ym: String): List<AccountEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccountItemEntity(accountItemEntity: AccountEntity)

    @Delete
    fun deleteAccountItemEntity(accountItemEntity: AccountEntity)

    @Update
    fun updateAccountItemEntity(accountItemEntity: AccountEntity)
}