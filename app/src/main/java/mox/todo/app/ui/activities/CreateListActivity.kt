package mox.todo.app.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mox.todo.app.R
import mox.todo.app.models.TodoList
import mox.todo.app.ui.viewmodels.CreateListViewModel

class CreateListActivity : ActivityBase() {

    private lateinit var viewModel: CreateListViewModel

    private lateinit var name: EditText
    private var color: Int = 0
    private lateinit var colorPicker: ColorPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_list)
        viewModel = ViewModelProviders.of(this).get(CreateListViewModel::class.java)

        name = findViewById(R.id.name)
        colorPicker = ColorPicker()

        setupNavigation()
    }

    private fun setupNavigation() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { createList() }
    }

    private fun createList() {
        if (viewModel.addTodoList(TodoList(
            name.text.toString(),
            color
        ))) finish()
        else
            Toast.makeText(this,
                "An error occurred",
                Toast.LENGTH_SHORT)
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    inner class ColorPicker {

        private val blue: RadioButton
        private val red: RadioButton
        private val orange: RadioButton
        private val green: RadioButton

        private var selected: RadioButton

        init {
            blue = findViewById(R.id.blue)
            red = findViewById(R.id.red)
            orange = findViewById(R.id.orange)
            green = findViewById(R.id.green)

            blue.setOnClickListener(this::onClick)
            red.setOnClickListener(this::onClick)
            orange.setOnClickListener(this::onClick)
            green.setOnClickListener(this::onClick)

            selected = blue // Kotlin compile error fix
            select(blue)
        }

        private fun onClick(v: View) {
            v as RadioButton
            select(v)
        }

        private fun select(b: RadioButton) {
            selected.isChecked = false
            b.isChecked = true
            selected = b
            when(b) {
                blue -> color = 0
                red -> color = 1
                orange -> color = 2
                green -> color = 3
            }
        }
    }
}
