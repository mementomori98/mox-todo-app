package mox.todo.app.api

import android.widget.Toast
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class ApiFactory {

    companion object {

        @PublishedApi internal val retrofit = Retrofit.Builder()
            .baseUrl("https://mox-todo-api.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build())
            .build()

        inline fun <reified TApi> build() = retrofit.create(TApi::class.java)
    }

}