package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entitiy.TodoEntity

@Dao
interface TodoDao {
    @Query("select * from `todo_list.db` where dueDate = date('now', 'localtime') and isComplete == false")
    fun getTodayTodoList(): List<TodoEntity>

    @Query("select * from `todo_list.db` where dueDate > date('now', 'localtime') order by dueDate")
    fun getFutureTodoList(): List<TodoEntity>

    @Query("select * from `todo_list.db` where dueDate = date('now', 'localtime') and isComplete == true order by dueDate")
    fun getTodayCompleteTodoList(): List<TodoEntity>

    @Query("select * from `todo_list.db` where dueDate < date('now', 'localtime') and isComplete == false order by dueDate")
    fun getPrevTodoList(): List<TodoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTodoEntity(todoEntity: TodoEntity)

    @Delete
    fun deleteTodoEntity(todoEntity: TodoEntity)

    @Update
    fun updateTodoList(todoEntity: TodoEntity)
}