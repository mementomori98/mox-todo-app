package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.models.Todo
import mox.todo.app.repositories.TodoApiRepository
import mox.todo.app.repositories.TodoListApiRepository
import mox.todo.app.repositories.TodoListRepository
import mox.todo.app.repositories.TodoRepository

class UpdateTodoViewModel : ViewModel {

    private val todoRepository: TodoRepository
    private val listRepository: TodoListRepository
    lateinit var todo: Todo

    constructor() : this(TodoApiRepository.instance, TodoListApiRepository.instance)

    constructor(todoRepository: TodoRepository, listRepository: TodoListRepository) {
        this.todoRepository = todoRepository
        this.listRepository = listRepository
    }

    fun updateTodo(todo: Todo) = todoRepository.update(todo)
    fun listNames() = listRepository.getAll().map { it.name }

}