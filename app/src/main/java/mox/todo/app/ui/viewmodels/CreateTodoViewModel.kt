package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.models.Todo
import mox.todo.app.repositories.TodoRepositoryImpl

class CreateTodoViewModel : ViewModel() {

    private val todoRepository = TodoRepositoryImpl.instance

    fun addTodo(todo: Todo): Boolean {
        todoRepository.add(todo)
        return true
    }

}