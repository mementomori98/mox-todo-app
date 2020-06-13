package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.repositories.TodoListApiRepository

class MainViewModel : ViewModel() {

    private val todoListRepository = TodoListApiRepository.instance

    var listId: Int? = null
    fun todoLists() = todoListRepository.liveData()

}