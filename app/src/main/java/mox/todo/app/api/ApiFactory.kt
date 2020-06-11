package mox.todo.app.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

class ApiFactory {

    companion object {

        @PublishedApi internal val retrofit = Retrofit.Builder()
            .baseUrl("localhost:8080/api")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        inline fun <reified TApi: Api> build() = retrofit.create(TApi::class.java)
    }

}

