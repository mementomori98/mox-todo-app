package mox.todo.app.ui.activities

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import mox.todo.app.Preferences
import mox.todo.app.R

abstract class ActivityBase : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setTheme(Preferences.instance.getTheme())
    }
}