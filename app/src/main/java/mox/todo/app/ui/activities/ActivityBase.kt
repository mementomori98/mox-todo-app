package mox.todo.app.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import mox.todo.app.Preferences
import mox.todo.app.R
import mox.todo.app.api.ApiFactory
import mox.todo.app.api.TodoApi
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

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null)
            startLoginActivity()
    }

    private fun startLoginActivity() {
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
}