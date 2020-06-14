package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.repositories.TodoListApiRepository
import mox.todo.app.repositories.TodoListRepository

class MainViewModel : ViewModel {

    private val listRepository: TodoListRepository

    constructor() : this(TodoListApiRepository.instance)

    constructor(listRepository: TodoListRepository) {
        this.listRepository = listRepository
    }

    var listId: Int? = null
    fun todoLists() = listRepository.liveData()

}