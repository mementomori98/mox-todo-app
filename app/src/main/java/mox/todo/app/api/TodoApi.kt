package mox.todo.app.api

import mox.todo.app.models.Todo
import mox.todo.app.models.TodoList
import retrofit2.Call
import retrofit2.http.*

interface TodoApi {

    @GET("todos")
    fun getTodos(): Call<List<Todo>>
    @POST("todos")
    fun addTodo(@Body todo: Todo): Call<Todo>
    @DELETE("todos/{key}")
    fun deleteTodo(@Path("key") key: Int): Call<Void>

    @GET("lists")
    fun getLists(): Call<List<TodoList>>
    @POST("lists")
    fun addList(@Body list: TodoList): Call<TodoList>
    @DELETE("lists/{key}")
    fun deleteList(@Path("key") key: Int): Call<Void>

}