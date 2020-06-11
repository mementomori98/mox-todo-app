package mox.todo.app.repositories

import mox.todo.app.models.TodoList
import mox.todo.app.util.LiveData
import mox.todo.app.util.MutableLiveData

class TodoListRepositoryImpl private constructor() : TodoListRepository {

    companion object {
        val instance = TodoListRepositoryImpl()
    }

    private val list: MutableList<TodoList> = ArrayList()
    private val liveData: MutableLiveData<List<TodoList>> = MutableLiveData()
    private var id = 1

    init {
        add(TodoList("Work", 0))
        add(TodoList("School", 2))
        add(TodoList("Shopping", 1))
        add(TodoList("Miscellaneous", 3))
        liveData.value = list
    }

    override fun liveData(): LiveData<List<TodoList>> {
        return liveData
    }

    override fun getAll(): List<TodoList> {
        return list
    }

    override fun getById(id: Int): TodoList {
        return list.find { it.id == id }
            ?: throw IllegalArgumentException("List id doesn't exist")
    }

    override fun add(todoList: TodoList): TodoList {
        todoList.id = id++
        list.add(todoList)
        liveData.value = list
        return todoList
    }

    override fun delete(id: Int): Boolean {
        val success = list.removeIf { it.id == id }
        if (success)
            liveData.value = list
        return success
    }

    override fun exists(id: Int): Boolean {
        return list.any { it.id == id }
    }
}