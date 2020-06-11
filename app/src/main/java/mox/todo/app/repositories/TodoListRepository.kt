package mox.todo.app.repositories

import mox.todo.app.models.TodoList
import mox.todo.app.util.LiveData

interface TodoListRepository {

    fun liveData(): LiveData<List<TodoList>>
    fun getAll(): List<TodoList>
    fun getById(id: Int): TodoList
    fun add(todoList: TodoList): TodoList
    fun delete(id: Int): Boolean
    fun exists(id: Int): Boolean

}