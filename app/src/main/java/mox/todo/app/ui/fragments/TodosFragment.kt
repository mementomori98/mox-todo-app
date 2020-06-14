package mox.todo.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mox.todo.app.R
import mox.todo.app.models.Todo
import mox.todo.app.ui.activities.CreateTodoActivity
import mox.todo.app.ui.activities.UpdateTodoActivity
import mox.todo.app.ui.viewmodels.TodosViewModel

class TodosFragment(private val listId: Int? = null, private val onListDeleteListener: () -> Unit = {}) : FragmentBase<TodosViewModel>() {

    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        root = getRoot(R.layout.fragment_todos, container, inflater)
        initViewModel<TodosViewModel>()
        viewModel.listId = listId

        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            if (!viewModel.hasLists()) {
                Toast.makeText(activity, resources.getString(R.string.no_list_error), Toast.LENGTH_LONG).show()
            }
            else {
                val bundle = Bundle()
                viewModel.listId?.let { bundle.putInt("listId", it) }
                val intent = Intent(activity, CreateTodoActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }

        setViewColors()
        setupRecyclerView()

        return root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_list -> {
                viewModel.deleteList()
                onListDeleteListener()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        val recyclerView = root.findViewById<RecyclerView>(R.id.rv)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        TodosRecyclerAdapter(
            viewModel.todos(),
            this::updateTodo,
            viewModel::deleteTodo,
            this::additionListener,
            this::mapColor,
            recyclerView,
            resources,
            getBooleanPreference("compact"),
            viewModel.listId != null,
            viewLifecycleOwner
        )
        resources
    }

    private fun additionListener(todo: Todo, position: Int) {
        viewModel.addTodo(todo, position)
    }

    private fun updateTodo(todo: Todo) {
        val intent = Intent(activity, UpdateTodoActivity::class.java)
        intent.putExtra("todo", todo)
        startActivity(intent)
    }

    private fun mapColor(listColor: Int) = when (listColor) {
        0 -> R.color.blue
        1 -> R.color.red
        2 -> R.color.orange
        3 -> R.color.green
        else -> R.id.blue
    }

    private fun setViewColors() {
        activity?.title = viewModel.listName()
        activity?.findViewById<Toolbar>(R.id.toolbar)
            ?.setBackgroundColor(
                resources.getColor(mapColor(viewModel.listColor()), null)
            )
        activity?.findViewById<View>(R.id.drawer)
            ?.setBackgroundColor(
                resources.getColor(mapColor(viewModel.listColor()), null)
            )
    }
}
