package mox.todo.app.repositories

import mox.todo.app.api.TodoApiClientImpl
import mox.todo.app.models.TodoList
import mox.todo.app.util.MutableLiveData

class TodoListApiRepository private constructor() : TodoListRepository {

    companion object {
        val instance: TodoListRepository = TodoListApiRepository()
    }

    private val liveData = MutableLiveData<List<TodoList>>()
    private val api = TodoApiClientImpl.instance
    private val todoRepository = TodoApiRepository.instance

    init {
        liveData.value = ArrayList() // must not be null at any point in time
        updateLiveData()
    }

    override fun liveData() = liveData

    override fun getAll() = liveData.value

    override fun getById(id: Int) = liveData.value.find { it.key == id }

    override fun add(todoList: TodoList): Boolean {
        if (todoList.name.trim() == "") return false
        api.addList(todoList) { updateLiveData() }
        return true
    }

    override fun delete(id: Int) = api.deleteList(id) { updateLiveData(); todoRepository.updateLiveData() }

    override fun exists(id: Int): Boolean = liveData.value.any { it.key == id }

    override fun updateLiveData() = api.getLists {
        if (it != null) liveData.value = it
    }
}