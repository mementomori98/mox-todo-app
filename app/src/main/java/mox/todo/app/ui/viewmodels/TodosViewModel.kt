package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.models.Todo
import mox.todo.app.repositories.TodoListRepositoryImpl
import mox.todo.app.repositories.TodoRepositoryImpl

class TodosViewModel : ViewModel() {

    private val todoRepository = TodoRepositoryImpl.instance
    private val todoListRepository = TodoListRepositoryImpl.instance

    var listId: Int? = null

    fun todos() = todoRepository.liveData(listId)
    fun listName() = (listId?.let{ todoListRepository.getById(it) })?.name ?: "All Todos"
    fun listName(listId: Int? = null) = (listId?.let {
        todoListRepository.getById(listId)
    } )?.name ?: ""
    fun listColor(listId: Int? = null): Int {
        val id = listId ?: this.listId
        return (id?.let{ todoListRepository.getById(it) })?.color ?: 0
    }

    fun deleteTodo(todo: Todo) = todoRepository.delete(todo.id)
    fun addTodo(todo: Todo, position: Int = 0) = todoRepository.add(todo, position)

}