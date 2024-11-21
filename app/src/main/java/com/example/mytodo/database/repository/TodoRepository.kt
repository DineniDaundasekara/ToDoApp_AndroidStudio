package com.example.mytodo.database.repository

import com.example.mytodo.database.TodoDatabase
import com.example.mytodo.database.entities.Todo

class TodoRepository (
    private val db: TodoDatabase
){
    suspend fun insert(todo: Todo) = db.getTodoDao().insertTodo(todo)
    suspend fun delete(todo:Todo) = db.getTodoDao().deleteTodo(todo)
    suspend fun update(todo: Todo) = db.getTodoDao().updateTodo(todo)
    fun getAllTodoItems():List<Todo> = db.getTodoDao().getAllTodoItems()



}