package mox.todo.app.api

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Credentials
import okhttp3.Interceptor
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
                .addInterceptor { chain ->
                    chain.proceed(
                        chain.request().newBuilder()
                            .header("Authorization", Credentials.basic(
                                FirebaseAuth.getInstance().currentUser?.uid ?: "", "1234"))
                            .build()
                    )
                }
                .build())
            .build()

        inline fun <reified TApi> build(): TApi = retrofit.create(TApi::class.java)
    }

}