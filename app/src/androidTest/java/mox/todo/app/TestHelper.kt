package mox.todo.app

import android.app.Activity
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import com.google.firebase.auth.FirebaseAuth

class TestHelper

fun authenticateTest(activity: Activity) {
    if (FirebaseAuth.getInstance().currentUser?.email != "test@test.com") {
        FirebaseAuth.getInstance().signOut()
        Thread.sleep(2000)
        FirebaseAuth.getInstance().signInWithEmailAndPassword("test@test.com", "asd123")
        Thread.sleep(2000)
        UiThreadStatement.runOnUiThread { activity.recreate() }
    }
}