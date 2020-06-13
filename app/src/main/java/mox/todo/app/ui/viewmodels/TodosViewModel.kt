package mox.todo.app.ui.viewmodels

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import mox.todo.app.models.Todo
import mox.todo.app.repositories.TodoApiRepository
import mox.todo.app.repositories.TodoListApiRepository

class TodosViewModel : ViewModel() {

    private val todoRepository = TodoApiRepository.instance
    private val todoListRepository = TodoListApiRepository.instance

    var listId: Int? = null

    fun todos() = todoRepository.liveData(listId)
    fun listName() = (listId?.let{ todoListRepository.getById(it) })?.name ?: "All Todos"
    fun listColor(): Int {
        if (listId == null) return 0
        return todoListRepository.getById(listId!!).color
    }

    fun deleteTodo(todo: Todo) = todoRepository.delete(todo.key)
    fun deleteList() = todoListRepository.delete(listId!!)
    fun addTodo(todo: Todo, position: Int = 0) = todoRepository.add(todo, position)

}