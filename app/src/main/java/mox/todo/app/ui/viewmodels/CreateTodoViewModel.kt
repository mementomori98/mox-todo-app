package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.models.Todo
import mox.todo.app.repositories.TodoApiRepository
import mox.todo.app.repositories.TodoListApiRepository

class CreateTodoViewModel : ViewModel() {

    private val todoRepository = TodoApiRepository.instance
    private val listRepository = TodoListApiRepository.instance

    var listId: Int? = null

    val listName: String get() = listId?.let { listRepository.getById(it).name } ?: ""

    fun addTodo(todo: Todo): Boolean {
        todoRepository.add(todo)
        return true
    }



}