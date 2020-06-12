package mox.todo.app.repositories

import mox.todo.app.api.ApiFactory
import mox.todo.app.api.TodoApi
import mox.todo.app.models.Todo
import mox.todo.app.models.TodoList
import mox.todo.app.util.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class TodoListApiRepository private constructor() : TodoListRepository {

    companion object {
        val instance = TodoListApiRepository()
    }

    private val liveData = MutableLiveData<List<TodoList>>()
    private val api = ApiFactory.build<TodoApi>()

    init {
        liveData.value = ArrayList() // must not be null
        updateLiveData()
    }

    override fun liveData() = liveData

    override fun getAll() = liveData.value

    override fun getById(id: Int) = liveData.value.find { it.key == id }
        ?: throw IllegalArgumentException()

    override fun add(todoList: TodoList) {
        api.addList(todoList).enqueue(object: Callback<TodoList> {
            override fun onFailure(call: Call<TodoList>, t: Throwable) {
            }

            override fun onResponse(call: Call<TodoList>, response: Response<TodoList>) {
                updateLiveData()
            }
        })
    }

    override fun delete(id: Int) = api.deleteList(id).enqueue(object: Callback<Void> {
        override fun onFailure(call: Call<Void>, t: Throwable) {
        }

        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            updateLiveData()
        }
    })

    override fun exists(id: Int): Boolean {
        return liveData.value.any { it.key == id }
    }

    private fun updateLiveData() {
        val call = api.getLists()
        call.enqueue(object : Callback<List<TodoList>> {
            override fun onFailure(call: Call<List<TodoList>>, t: Throwable) {
                throw t
            }

            override fun onResponse(call: Call<List<TodoList>>, response: Response<List<TodoList>>) {
                response.body()?.let {
                    liveData.value = it
                } ?: throw Exception()
            }

        })
    }
}