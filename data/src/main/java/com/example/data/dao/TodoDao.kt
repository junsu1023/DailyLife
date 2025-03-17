package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entitiy.TodoEntity

@Dao
interface TodoDao {
    @Query("select * from `todo_list.db`")
    fun getTodoList(): List<TodoEntity>

    @Query("select * from `todo_list.db` where updateDate = datetime('now', 'localtime')")
    fun getTodayTodoList(): List<TodoEntity>

    @Query("select * from `todo_list.db` where updateDate > datetime('now', 'localtime')")
    fun getFutureTodoList(): List<TodoEntity>

    @Query("select * from `todo_list.db` where updateDate = datetime('now', 'localtime') and state == 2")
    fun getTodayCompleteTodoList(): List<TodoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTodoEntity(todoEntity: TodoEntity)

    @Delete
    fun deleteTodoEntity(todoEntity: TodoEntity)
}