package mox.todo.app.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import mox.todo.app.R
import java.util.*

class StartupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
    }

    override fun onResume() {
        super.onResume()

        if (authenticated()) {
            startMainActivity()
            finish()
        }
        else
            startLoginActivity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (!authenticated())
                startLoginActivity()
            else {
                startMainActivity()
                finish()
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    protected fun startLoginActivity() {
        val providers = Arrays.asList(
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
