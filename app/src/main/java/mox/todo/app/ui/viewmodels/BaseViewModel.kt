package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.repositories.TodoApiRepository
import mox.todo.app.repositories.TodoListApiRepository
import mox.todo.app.repositories.TodoListRepository
import mox.todo.app.repositories.TodoRepository

class BaseViewModel : ViewModel {

    private val todoRepository: TodoRepository
    private val listRepository: TodoListRepository

    constructor() : this(TodoApiRepository.instance, TodoListApiRepository.instance)

    constructor(todoRepository: TodoRepository, listRepository: TodoListRepository) : super() {
        this.todoRepository = todoRepository
        this.listRepository = listRepository
    }

    fun updateLiveDatas() {
        todoRepository.updateLiveData()
        listRepository.updateLiveData()
    }
}