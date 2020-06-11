package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.models.TodoList
import mox.todo.app.repositories.TodoListRepositoryImpl

class CreateListViewModel : ViewModel() {

    private val todoListRepository = TodoListRepositoryImpl.instance

    fun addTodoList(list: TodoList): Boolean {
        todoListRepository.add(list)
        return true
    }

}
