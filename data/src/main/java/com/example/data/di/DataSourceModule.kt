package com.example.data.di

import com.example.data.dao.AccountDao
import com.example.data.dao.TodoDao
import com.example.data.datasource.AccountDataSource
import com.example.data.datasource.TodoDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideTodoDataSource(
        todoDao: TodoDao
    ): TodoDataSource = TodoDataSource(todoDao)

    @Provides
    @Singleton
    fun provideAccountDataSource(
        accountDao: AccountDao
    ): AccountDataSource = AccountDataSource(accountDao)
}