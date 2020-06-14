package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.models.TodoList
import mox.todo.app.repositories.TodoListApiRepository
import mox.todo.app.repositories.TodoListRepository

class CreateListViewModel : ViewModel {

    private val listRepository: TodoListRepository

    constructor() : this(TodoListApiRepository.instance)

    constructor(listRepository: TodoListRepository) {
        this.listRepository = listRepository
    }

    fun addTodoList(list: TodoList): Boolean {
        return listRepository.add(list)
    }

}
