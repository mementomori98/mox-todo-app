package mox.todo.app.repositories

import android.util.Log
import android.widget.Toast
import mox.todo.app.api.Api
import mox.todo.app.api.ApiFactory
import mox.todo.app.api.TodoApi
import mox.todo.app.models.Todo
import mox.todo.app.util.LiveData
import mox.todo.app.util.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoApiRepository private constructor() : TodoRepository {

    companion object {
        val instance = TodoApiRepository()
    }

    private val liveData = MutableLiveData<List<Todo>>()
    private val api = ApiFactory.build<TodoApi>()

    init {
        liveData.value = ArrayList()
        updateLiveData()
    }

    override fun liveData() = liveData

    override fun add(todo: Todo, position: Int) {
        api.addTodo(todo).enqueue(object : Callback<Todo> {
            override fun onFailure(call: Call<Todo>, t: Throwable) {
                Log.d("API", "Add todo failure")
            }

            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                Log.d("API", "Add todo success")
                updateLiveData()
            }
        })
    }

    override fun delete(id: Int) = api.deleteTodo(id).enqueue(object : Callback<Void> {
        override fun onFailure(call: Call<Void>, t: Throwable) {
        }

        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            updateLiveData()
        }
    })

    private fun updateLiveData() {
        api.getTodos().enqueue(object : Callback<List<Todo>> {
            override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                throw t
            }

            override fun onResponse(call: Call<List<Todo>>, response: Response<List<Todo>>) {
                response.body()?.let {
                    liveData.value = it
                } ?: throw Exception()
            }

        })
    }
}