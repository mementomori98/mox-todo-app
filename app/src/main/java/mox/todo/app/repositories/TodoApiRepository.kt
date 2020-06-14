package mox.todo.app.repositories

import android.util.Log
import mox.todo.app.api.ApiFactory
import mox.todo.app.api.TodoApi
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
        private var _instance: TodoRepository? = null

        val instance: TodoRepository get() {
            if (_instance == null)
                _instance = TodoApiRepository()
            return _instance!!
        }
    }

    private val liveData = MutableLiveData<List<Todo>>()
    private val liveDataMap = HashMap<String, MutableLiveData<List<Todo>>>()
    private val api = ApiFactory.build<TodoApi>()

    init {
        liveData.value = ArrayList()
        updateLiveData()
    }

    override fun liveData(list: String?): LiveData<List<Todo>> {
        if (list == null) return liveData
        return liveDataMap[list] ?: createListLiveData(list)
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

    override fun add(todo: Todo, position: Int): Boolean {
        if (todo.title == "") return false
        todo.position = position
        api.addTodo(todo).enqueue(object : Callback<Todo> {
            override fun onFailure(call: Call<Todo>, t: Throwable) {
                Log.d("API", "Add todo failure")
            }

            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                if (response.isSuccessful)
                    updateLiveData()
                else
                    Log.d("API", "Add todo failed")
            }
        })
        return true
    }

    override fun update(todo: Todo): Boolean {
        if (todo.title == "") return false
        api.updateTodo(todo).enqueue(object : Callback<Todo> {
            override fun onFailure(call: Call<Todo>, t: Throwable) {
                Log.d("API", "Add todo failure")
            }

            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                if (response.isSuccessful)
                    updateLiveData()
                else
                    Log.d("API", "Update todo failed")
            }
        })
        return true
    }

    override fun delete(id: Int) = api.deleteTodo(id).enqueue(object : Callback<Void> {
        override fun onFailure(call: Call<Void>, t: Throwable) {
        }

        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            updateLiveData()
        }
    })

    override fun updateLiveData() {
        api.getTodos().enqueue(object : Callback<List<Todo>> {
            override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                throw t
            }

            override fun onResponse(call: Call<List<Todo>>, response: Response<List<Todo>>) {
                response.body()?.let {
                    val list = it.sortedBy { t -> t.list?.toLowerCase(Locale.ROOT) ?: "a" }
                    liveData.value = list
                    liveDataMap.forEach { (list, data) ->
                        data.value = filterLiveDataByList(list)
                    }
                }
            }

        })
    }
}