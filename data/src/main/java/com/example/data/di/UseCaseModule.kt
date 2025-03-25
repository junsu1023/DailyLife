package com.example.data.di

import com.example.domain.repository.TodoRepository
import com.example.domain.usecase.AddTodoUseCase
import com.example.domain.usecase.DeleteTodoUseCase
import com.example.domain.usecase.GetFutureTodoListUseCase
import com.example.domain.usecase.GetPrevTodoListUseCase
import com.example.domain.usecase.GetTodayCompleteTodoListUseCase
import com.example.domain.usecase.GetTodayTodoListUseCase
import com.example.domain.usecase.UpdateTodoListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetTodayTodoListUseCase(todoRepository: TodoRepository) = GetTodayTodoListUseCase(todoRepository)

    @Provides
    fun provideGetFutureTodoListUseCase(todoRepository: TodoRepository) = GetFutureTodoListUseCase(todoRepository)

    @Provides
    fun provideGetTodayCompleteTodoListUseCase(todoRepository: TodoRepository) = GetTodayCompleteTodoListUseCase(todoRepository)

    @Provides
    fun provideGetPrevTodoListUseCase(todoRepository: TodoRepository) = GetPrevTodoListUseCase(todoRepository)

    @Provides
    fun provideAddTodoUseCase(todoRepository: TodoRepository) = AddTodoUseCase(todoRepository)

    @Provides
    fun provideDeleteTodoUseCase(todoRepository: TodoRepository) = DeleteTodoUseCase(todoRepository)

    @Provides
    fun provideUpdateTodoUseCase(todoRepository: TodoRepository) = UpdateTodoListUseCase(todoRepository)
}