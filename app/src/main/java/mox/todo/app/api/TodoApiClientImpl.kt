package mox.todo.app.api

import android.util.Log
import mox.todo.app.models.Todo
import mox.todo.app.models.TodoList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoApiClientImpl private constructor() : TodoApiClient {

    private val api = ApiFactory.build<TodoApi>()

    companion object {
        val instance = TodoApiClientImpl()
    }

    override fun getTodos(callback: (List<Todo>?) -> Unit) {
        api.getTodos().enqueue(getCallback(callback))
    }

    override fun addTodo(todo: Todo, callback: (Todo?) -> Unit) {
        api.addTodo(todo).enqueue(getCallback(callback))
    }

    override fun updateTodo(todo: Todo, callback: (Todo?) -> Unit) {
        api.updateTodo(todo).enqueue(getCallback(callback))
    }

    override fun deleteTodo(key: Int, callback: () -> Unit) {
        api.deleteTodo(key).enqueue(getCallback { callback() })
    }

    override fun getLists(callback: (List<TodoList>?) -> Unit) {
        api.getLists().enqueue(getCallback(callback))
    }

    override fun addList(list: TodoList, callback: (TodoList?) -> Unit) {
        api.addList(list).enqueue(getCallback(callback))
    }

    override fun deleteList(key: Int, callback: () -> Unit) {
        api.deleteList(key).enqueue(getCallback { callback() })
    }

    private fun<TResponse> getCallback(callback: (TResponse?) -> Unit) = object: Callback<TResponse?> {
        override fun onFailure(call: Call<TResponse?>, t: Throwable) {
            Log.d("API", "Request failed: ${t.message}")
            callback.invoke(null)
        }

        override fun onResponse(call: Call<TResponse?>, response: Response<TResponse?>) {
            Log.d("API", "Response code: ${response.code()}")
            callback.invoke(response.body())
        }
    }
}