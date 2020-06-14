package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.models.Todo
import mox.todo.app.repositories.TodoApiRepository
import mox.todo.app.repositories.TodoListApiRepository
import mox.todo.app.repositories.TodoListRepository
import mox.todo.app.repositories.TodoRepository

class TodosViewModel : ViewModel {

    private val todoRepository: TodoRepository
    private val listRepository: TodoListRepository
    var listId: Int? = null

    constructor() : this(TodoApiRepository.instance, TodoListApiRepository.instance)

    constructor(todoRepository: TodoRepository, listRepository: TodoListRepository) {
        this.todoRepository = todoRepository
        this.listRepository = listRepository
    }

    fun hasLists() = !listRepository.getAll().isEmpty()
    fun todos() = todoRepository.liveData(if (listId == null) null else listName())
    fun listName() = (listId?.let{ listRepository.getById(it) })?.name ?: "All Todos"

    fun listColor(): Int {
        if (listId == null) return 0
        return listRepository.getById(listId!!)?.color ?: 0
    }

    fun deleteTodo(todo: Todo) = todoRepository.delete(todo.key)
    fun deleteList() = listRepository.delete(listId!!)
    fun addTodo(todo: Todo, position: Int = 0) = todoRepository.add(todo, position)

}