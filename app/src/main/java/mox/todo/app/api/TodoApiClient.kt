package mox.todo.app.api

import mox.todo.app.models.Todo
import mox.todo.app.models.TodoList

interface TodoApiClient {

    fun getTodos(callback: (List<Todo>?) -> Unit)
    fun addTodo(todo: Todo, callback: (Todo?) -> Unit)
    fun updateTodo(todo: Todo, callback: (Todo?) -> Unit)
    fun deleteTodo(key: Int, callback: () -> Unit)

    fun getLists(callback: (List<TodoList>?) -> Unit)
    fun addList(list: TodoList, callback: (TodoList?) -> Unit)
    fun deleteList(key: Int, callback: () -> Unit)

}