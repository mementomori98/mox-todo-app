package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.models.Todo
import mox.todo.app.repositories.TodoApiRepository
import mox.todo.app.repositories.TodoListApiRepository
import mox.todo.app.repositories.TodoListRepository
import mox.todo.app.repositories.TodoRepository

class CreateTodoViewModel : ViewModel {

    private val todoRepository: TodoRepository
    private val listRepository: TodoListRepository

    constructor() : this(TodoApiRepository.instance, TodoListApiRepository.instance)

    constructor(todoRepository: TodoRepository, listRepository: TodoListRepository) {
        this.todoRepository = todoRepository
        this.listRepository = listRepository
    }

    var listId: Int? = null

    val listName: String get() = listId?.let { listRepository.getById(it)?.name } ?: ""

    fun addTodo(todo: Todo) = todoRepository.add(todo)

    fun listNames() = listRepository.getAll().map { it.name }

}