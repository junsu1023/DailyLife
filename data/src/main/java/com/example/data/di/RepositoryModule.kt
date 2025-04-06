package com.example.data.di

import com.example.data.datasource.AccountDataSource
import com.example.data.datasource.ClassificationDataSource
import com.example.data.datasource.TodoDataSource
import com.example.data.repository.AccountRepositoryImpl
import com.example.data.repository.ClassificationRepositoryImpl
import com.example.data.repository.TodoRepositoryImpl
import com.example.domain.repository.AccountRepository
import com.example.domain.repository.ClassificationRepository
import com.example.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTodoRepository(
        todoDataSource: TodoDataSource
    ): TodoRepository {
        return TodoRepositoryImpl(todoDataSource)
    }

    @Provides
    @Singleton
    fun provideAccountRepository(
        accountDataSource: AccountDataSource
    ): AccountRepository  {
        return AccountRepositoryImpl(accountDataSource)
    }

    @Provides
    @Singleton
    fun provideClassificationRepository(
        classificationDataSource: ClassificationDataSource
    ): ClassificationRepository {
        return ClassificationRepositoryImpl(classificationDataSource)
    }
}