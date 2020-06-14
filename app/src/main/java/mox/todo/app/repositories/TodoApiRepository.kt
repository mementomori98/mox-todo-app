package mox.todo.app.repositories

import android.util.Log
import mox.todo.app.api.ApiFactory
import mox.todo.app.api.TodoApi
import mox.todo.app.api.TodoApiClientImpl
import mox.todo.app.models.Todo
import mox.todo.app.util.LiveData
import mox.todo.app.util.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TodoApiRepository private constructor() : TodoRepository {

    companion object {
        val instance: TodoRepository = TodoApiRepository()
    }

    private val liveData = MutableLiveData<List<Todo>>()
    private val liveDataMap = HashMap<String, MutableLiveData<List<Todo>>>()
    private val api = TodoApiClientImpl.instance

    init {
        liveData.value = ArrayList() // must not be null at any point in time
        updateLiveData()
    }

    override fun liveData(list: String?): LiveData<List<Todo>> {
        if (list == null) return liveData
        return liveDataMap[list] ?: createListLiveData(list)
    }

    override fun add(todo: Todo, position: Int): Boolean {
        if (todo.title == "") return false
        todo.position = position
        api.addTodo(todo) { updateLiveData() }
        return true
    }

    override fun update(todo: Todo): Boolean {
        if (todo.title == "") return false
        api.updateTodo(todo) { updateLiveData() }
        return true
    }

    override fun delete(id: Int) = api.deleteTodo(id) { updateLiveData() }

    override fun updateLiveData() = api.getTodos {
        if (it != null) {
            val list = it.sortedBy { t -> t.list?.toLowerCase(Locale.ROOT) }
            liveData.value = list
            liveDataMap.forEach { (list, data) ->
                data.value = filterLiveDataByList(list)
            }
        }
    }

    private fun createListLiveData(list: String): MutableLiveData<List<Todo>> {
        val liveData = MutableLiveData<List<Todo>>()
        liveData.value = filterLiveDataByList(list)
        liveDataMap[list] = liveData
        return liveData
    }

    private fun filterLiveDataByList(list: String): List<Todo> {
        return this.liveData.value.filter {
            it.list == list
        }
    }
}