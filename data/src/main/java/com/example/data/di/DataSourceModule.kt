package com.example.data.di

import com.example.data.dao.CalendarDao
import com.example.data.dao.TodoDao
import com.example.data.datasource.CalendarDataSource
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
    fun provideCalendarDataSource(
        calendarDao: CalendarDao
    ): CalendarDataSource = CalendarDataSource((calendarDao))
}