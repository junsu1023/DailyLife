package com.example.data.di

import com.example.data.datasource.TodoDataSource
import com.example.data.repository.TodoRepositoryImpl
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
}