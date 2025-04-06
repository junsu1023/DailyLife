package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.database.AccountDatabase
import com.example.data.database.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTodoListDatabase(@ApplicationContext context: Context): TodoDatabase =
        Room.databaseBuilder(
            context = context,
            klass = TodoDatabase::class.java,
            name = "todo_list.db"
        ).build()

    @Provides
    fun provideTodoListDao(todoDatabase: TodoDatabase) = todoDatabase.todoDao()

    @Provides
    @Singleton
    fun provideAccountDatabase(@ApplicationContext context: Context): AccountDatabase =
        AccountDatabase.getInstance(context)

    @Provides
    fun provideAccountDao(accountDatabase: AccountDatabase) = accountDatabase.accountDao()

    @Provides
    fun provideClassificationDao(accountDatabase: AccountDatabase) = accountDatabase.classificationDao()
}