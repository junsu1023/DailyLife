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
    @Query("select * from account_db where ym == :ym order by ym")
    fun getCurrentYMAccountInfo(ym: String): List<AccountEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccountItemEntity(accountItemEntity: AccountEntity)

    @Delete
    fun deleteAccountItemEntity(accountItemEntity: AccountEntity)

    @Update
    fun updateAccountItemEntity(accountItemEntity: AccountEntity)
}