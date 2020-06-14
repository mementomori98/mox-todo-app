package mox.todo.app.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import mox.todo.app.R
import mox.todo.app.models.Todo
import mox.todo.app.repositories.TodoApiRepository
import mox.todo.app.repositories.TodoListApiRepository

class TodosViewModel(app: Application) : AndroidViewModel(app) {

    private val todoRepository = TodoApiRepository.instance
    private val todoListRepository = TodoListApiRepository.instance
    private val allTodosString get() = getApplication<Application>().resources.getString(R.string.all_todos)
    var listId: Int? = null

    fun hasLists() = !todoListRepository.getAll().isEmpty()
    fun todos() = todoRepository.liveData(if (listId == null) null else listName())
    fun listName() = (listId?.let{ todoListRepository.getById(it) })?.name ?: allTodosString


    fun listColor(): Int {
        if (listId == null) return 0
        return todoListRepository.getById(listId!!).color
    }

    fun deleteTodo(todo: Todo) = todoRepository.delete(todo.key)
    fun deleteList() = todoListRepository.delete(listId!!)
    fun addTodo(todo: Todo, position: Int = 0) = todoRepository.add(todo, position)

}