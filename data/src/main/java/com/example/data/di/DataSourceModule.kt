package com.example.data.di

import com.example.data.dao.AccountDao
import com.example.data.dao.CardCompanyDao
import com.example.data.dao.ClassificationDao
import com.example.data.dao.TodoDao
import com.example.data.datasource.AccountDataSource
import com.example.data.datasource.CardCompanyDataSource
import com.example.data.datasource.ClassificationDataSource
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

    @Provides
    fun provideClassificationDataSource(
        classificationDao: ClassificationDao
    ): ClassificationDataSource = ClassificationDataSource(classificationDao)

    @Provides
    fun provideCardCompanyDataSource(
        cardCompanyDao: CardCompanyDao
    ): CardCompanyDataSource = CardCompanyDataSource(cardCompanyDao)
}