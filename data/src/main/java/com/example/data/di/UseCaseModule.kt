package com.example.data.di

import com.example.domain.repository.AccountRepository
import com.example.domain.repository.ClassificationRepository
import com.example.domain.repository.TodoRepository
import com.example.domain.usecase.account.AddAccountItemUseCase
import com.example.domain.usecase.todo.AddTodoUseCase
import com.example.domain.usecase.account.DeleteAccountItemUseCase
import com.example.domain.usecase.todo.DeleteTodoUseCase
import com.example.domain.usecase.todo.GetAllTodoListUseCase
import com.example.domain.usecase.todo.GetCompleteTodoListOfDateUseCase
import com.example.domain.usecase.todo.GetTodoListOfDateUseCase
import com.example.domain.usecase.todo.GetTodoListOfFutureUseCase
import com.example.domain.usecase.todo.GetTodoListOfPrevUseCase
import com.example.domain.usecase.todo.GetCompleteTodoListOfTodayUseCase
import com.example.domain.usecase.account.GetCurrentYMAccountInfoUseCase
import com.example.domain.usecase.todo.GetTodoListOfTodayUseCase
import com.example.domain.usecase.account.UpdateAccountItemUseCase
import com.example.domain.usecase.classification.AddClassificationUseCase
import com.example.domain.usecase.classification.DeleteClassificationUseCase
import com.example.domain.usecase.classification.GetClassificationListUseCase
import com.example.domain.usecase.classification.UpdateClassificationUseCase
import com.example.domain.usecase.todo.UpdateTodoListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    // Calendar & Todo
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

    // Account
    @Provides
    fun provideGetCurrentYMAccountInfoUseCase(accountRepository: AccountRepository) = GetCurrentYMAccountInfoUseCase(accountRepository)

    @Provides
    fun provideAddAccountItemUseCase(accountRepository: AccountRepository) = AddAccountItemUseCase(accountRepository)

    @Provides
    fun provideDeleteAccountItemUseCase(accountRepository: AccountRepository) = DeleteAccountItemUseCase(accountRepository)

    @Provides
    fun provideUpdateAccountItemUseCase(accountRepository: AccountRepository) = UpdateAccountItemUseCase(accountRepository)

    // Classification
    @Provides
    fun provideGetClassificationListUseCase(classificationRepository: ClassificationRepository) = GetClassificationListUseCase(classificationRepository)

    @Provides
    fun provideAddClassificationUseCase(classificationRepository: ClassificationRepository) = AddClassificationUseCase(classificationRepository)

    @Provides
    fun provideDeleteClassificationUseCase(classificationRepository: ClassificationRepository) = DeleteClassificationUseCase(classificationRepository)

    @Provides
    fun provideUpdateClassificationUseCase(classificationRepository: ClassificationRepository) = UpdateClassificationUseCase(classificationRepository)
}