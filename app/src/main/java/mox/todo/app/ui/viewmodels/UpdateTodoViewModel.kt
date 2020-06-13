package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.models.Todo
import mox.todo.app.repositories.TodoApiRepository
import mox.todo.app.repositories.TodoListApiRepository

class UpdateTodoViewModel : ViewModel() {

    private val todoRepository = TodoApiRepository.instance
    private val listRepository = TodoListApiRepository.instance

    lateinit var todo: Todo

    fun updateTodo(todo: Todo) = todoRepository.update(todo)

    fun listNames() = listRepository.getAll().map { it.name }

}