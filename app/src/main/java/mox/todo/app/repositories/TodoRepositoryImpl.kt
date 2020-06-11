package mox.todo.app.repositories

import mox.todo.app.util.LiveData
import mox.todo.app.util.MutableLiveData
import mox.todo.app.models.Todo

class TodoRepositoryImpl private constructor() : TodoRepository {

    companion object {
        val instance = TodoRepositoryImpl()
    }

    private val todoListRepository = TodoListRepositoryImpl.instance

    private val todos: MutableList<Todo> = ArrayList()
    private val allLiveData: MutableLiveData<List<Todo>> = MutableLiveData()
    private val listLiveData: MutableMap<Int, MutableLiveData<List<Todo>>> = HashMap()

    private var id = 0

    init {
        add(Todo("Ask for vacation", "July 1st to 15th", 3, 1))
        add(Todo("Ask for a raise", "", 5, 1))

        add(Todo("ADS", "Thursday 9-12", 2, 2))
        add(Todo("DAI", "17th Wednesday 9.00 - 18th Thursday 9.00", 3, 2))
        add(Todo("AND", "Sunday 23.59", 4, 2))

        add(Todo("Tomatoes", "Organic is better", 1, 3))
        add(Todo("Bell peppers", "", 1, 3))
        add(Todo("Salt & Pepper", "", 1, 3))
        add(Todo("Rice", "", 1, 3))
        add(Todo("Pasta", "", 1, 3))
        add(Todo("Muesli", "", 1, 3))

        add(Todo("Call mom", "", 1, 4))
        add(Todo("Call dad", "", 1, 4))
        add(Todo("Do laundry", "", 1, 4))
        add(Todo("Watch Clean code", "", 1, 4))
        add(Todo("Go on a walk with Ralu", "", 1, 4))

        allLiveData.value = todos
    }

    override fun liveData(listId: Int?): LiveData<List<Todo>> {
        if (listId == null) return allLiveData
        if (!todoListRepository.exists(listId)) throw IllegalStateException("Todo list $listId doesn't exist")
        createLiveDataListIfNotExists(listId)
        return listLiveData[listId]!!
    }

    override fun add(todo: Todo, position: Int): Todo {
        todo.title = todo.title.trim()
        if (todo.title == "") throw IllegalArgumentException("Title cannot be empty")
        if (todo.priority < 1 || todo.priority > 5) throw IllegalArgumentException("Priority must be between 1 and 5")
        todo.id = id++

        return if (todo.listId == null)
            addTodoWithoutList(todo, position)
        else addTodoToList(todo, position)
    }

    private fun addTodoToList(todo: Todo, position: Int): Todo {
        if (todo.listId == null) throw IllegalArgumentException("List id cannot be null")
        if (!todoListRepository.exists(todo.listId!!)) throw IllegalStateException("Todo list ${todo.listId} doesn't exist")
        todos.add(position, todo)
        updateLiveData(todo)
        return todo
    }

    private fun addTodoWithoutList(todo: Todo, position: Int): Todo {
        if (todo.listId != null) throw IllegalArgumentException("List id must be null")
        todos.add(position, todo)
        updateLiveData(todo)
        return todo
    }

    private fun updateLiveData(todo: Todo) {
        allLiveData.value = todos
        if (todo.listId == null) return
        createLiveDataListIfNotExists(todo.listId!!)
        listLiveData[todo.listId!!]!!.value = todos.filter { it.listId == todo.listId }
    }

    private fun createLiveDataListIfNotExists(listId: Int) {
        if (listLiveData[listId] == null) {
            listLiveData[listId] = MutableLiveData()
            listLiveData[listId]!!.value = ArrayList()
        }
    }

    override fun delete(id: Int): Boolean {
        val todo = todos.find { it.id == id }
        val success = todos.removeIf { it.id == id }
        if (success)
            updateLiveData(todo!!)
        return success
    }
}