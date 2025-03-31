package com.example.data.di

import com.example.domain.repository.TodoRepository
import com.example.domain.usecase.AddTodoUseCase
import com.example.domain.usecase.DeleteTodoUseCase
import com.example.domain.usecase.GetAllTodoListUseCase
import com.example.domain.usecase.GetCompleteTodoListOfDateUseCase
import com.example.domain.usecase.GetTodoListOfDateUseCase
import com.example.domain.usecase.GetTodoListOfFutureUseCase
import com.example.domain.usecase.GetTodoListOfPrevUseCase
import com.example.domain.usecase.GetCompleteTodoListOfTodayUseCase
import com.example.domain.usecase.GetTodoListOfTodayUseCase
import com.example.domain.usecase.UpdateTodoListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetAllTodoListUseCase(todoRepository: TodoRepository) = GetAllTodoListUseCase(todoRepository)

    @Provides
    fun provideGetTodoListOfTodayUseCase(todoRepository: TodoRepository) = GetTodoListOfTodayUseCase(todoRepository)

    @Provides
    fun provideGetTodoListOfFutureUseCase(todoRepository: TodoRepository) = GetTodoListOfFutureUseCase(todoRepository)

    @Provides
    fun provideGetCompleteTodoListOfTodayUseCase(todoRepository: TodoRepository) = GetCompleteTodoListOfTodayUseCase(todoRepository)

    @Provides
    fun provideGetTodoListOfPrevUseCase(todoRepository: TodoRepository) = GetTodoListOfPrevUseCase(todoRepository)

    @Provides
    fun provideGetTodoListOfDateUseCase(todoRepository: TodoRepository) = GetTodoListOfDateUseCase(todoRepository)

    @Provides
    fun provideGetCompleteTodoListOfDate(todoRepository: TodoRepository) = GetCompleteTodoListOfDateUseCase(todoRepository)

    @Provides
    fun provideAddTodoUseCase(todoRepository: TodoRepository) = AddTodoUseCase(todoRepository)

    @Provides
    fun provideDeleteTodoUseCase(todoRepository: TodoRepository) = DeleteTodoUseCase(todoRepository)

    @Provides
    fun provideUpdateTodoUseCase(todoRepository: TodoRepository) = UpdateTodoListUseCase(todoRepository)
}