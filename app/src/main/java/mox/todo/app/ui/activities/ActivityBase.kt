package mox.todo.app.ui.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import mox.todo.app.Preferences
import java.util.Arrays.asList

abstract class ActivityBase : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setTheme(Preferences.instance.getTheme())
    }

    override fun onResume() {
        super.onResume()

        if (!authenticated())
            startLoginActivity()
    }

    protected fun startLoginActivity() {
        val providers = asList(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        startActivityForResult(intent, 1)
    }

    protected fun authenticated() = FirebaseAuth.getInstance().currentUser != null
}