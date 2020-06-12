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

    fun todos() = todoRepository.liveData()
    fun listName() = (listId?.let{ todoListRepository.getById(it) })?.name ?: "All Todos"
    fun listColor(listId: Int? = null): Int {
        val id = listId ?: this.listId
        return (id?.let{ todoListRepository.getById(it) })?.color ?: 0
    }

    fun deleteTodo(todo: Todo) = todoRepository.delete(todo.key)
    fun addTodo(todo: Todo, position: Int = 0) = todoRepository.add(todo, position)

}