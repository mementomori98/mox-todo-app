package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.models.Todo
import mox.todo.app.repositories.TodoListRepositoryImpl
import mox.todo.app.repositories.TodoRepositoryImpl
import mox.todo.app.util.LiveData

class MainViewModel : ViewModel() {

    private val todoListRepository = TodoListRepositoryImpl.instance

    fun todoLists() = todoListRepository.liveData()

}