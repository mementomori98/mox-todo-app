package mox.todo.app.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mox.todo.app.R
import mox.todo.app.models.Todo
import mox.todo.app.ui.viewmodels.CreateTodoViewModel

class CreateTodoActivity : ActivityBase() {

    private lateinit var viewModel: CreateTodoViewModel
    private lateinit var title: EditText
    private lateinit var notes: EditText
    private lateinit var priority: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)
        viewModel = ViewModelProviders.of(this).get(CreateTodoViewModel::class.java)
        intent.extras?.getInt("listId").let {
            viewModel.listId = if(it == 0) null else it
        }
        initializeViews()
        setupNavigation()
    }

    private fun initializeViews() {
        title = findViewById(R.id.title)
        notes = findViewById(R.id.notes)
        priority = findViewById(R.id.priority)
        priority.minValue = 1
        priority.maxValue = 5
        priority.wrapSelectorWheel = false
    }

    private fun setupNavigation() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { createTodo() }
    }

    private fun createTodo() {
        if (viewModel.addTodo(Todo(
            title.text.toString(),
            notes.text.toString(),
            priority.value,
            viewModel.listName))
        )
            finish()
        else
            Toast.makeText(this,
                getString(R.string.todo_create_error),
                Toast.LENGTH_SHORT)
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
