package mox.todo.app.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mox.todo.app.R
import mox.todo.app.models.Todo
import mox.todo.app.ui.viewmodels.UpdateTodoViewModel

class UpdateTodoActivity : AppCompatActivity() {

    private lateinit var viewModel: UpdateTodoViewModel
    private lateinit var title: EditText
    private lateinit var notes: EditText
    private lateinit var priority: NumberPicker
    private lateinit var list: Spinner
    private lateinit var todo: Todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_todo)
        viewModel = ViewModelProviders.of(this).get(UpdateTodoViewModel::class.java)
        viewModel.todo = intent.extras?.getSerializable("todo") as Todo
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
        list = findViewById(R.id.list)
        list.adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, viewModel.listNames())
        val index = viewModel.listNames().indexOfFirst { it == viewModel.todo.list }
        list.setSelection(if (index == -1) 0 else index)

        title.setText(viewModel.todo.title)
        notes.setText(viewModel.todo.notes)
        priority.value = viewModel.todo.priority
    }

    private fun setupNavigation() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { updateTodo() }
    }

    private fun updateTodo() {
        if (viewModel.updateTodo(
                Todo(
                title.text.toString(),
                notes.text.toString(),
                priority.value,
                list.selectedItem?.toString(),
                0,
                viewModel.todo.key)
            )
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
