package mox.todo.app.api

import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

class ApiFactory {

    companion object {

        @PublishedApi internal val retrofit = Retrofit.Builder()
            .baseUrl("https://mox-todo-api.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        inline fun <reified TApi> build() = retrofit.create(TApi::class.java)
    }

}