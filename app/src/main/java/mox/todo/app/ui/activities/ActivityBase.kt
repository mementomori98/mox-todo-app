package mox.todo.app.ui.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import mox.todo.app.R
import java.util.*
import java.util.Arrays.asList

abstract class ActivityBase : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setTheme(readTheme())
    }

    private fun setLanguage() {
        val locale = Locale(getSharedPreferences("pref", 0).getString("lang", "en")!!)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(
            configuration,
            baseContext.resources.displayMetrics
        )
    }

    private fun readTheme(): Int {
        val prefs = getSharedPreferences("pref", MODE_PRIVATE)
        return when (prefs.getString("theme", "dark")) {
            "dark" -> R.style.AppTheme_Dark
            "light" -> R.style.AppTheme_Light
            else -> R.style.AppTheme_Light
        }
    }

    override fun onResume() {
        super.onResume()
        if (!authenticated())
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

        startActivity(intent)
    }

    private fun authenticated() = FirebaseAuth.getInstance().currentUser != null
}