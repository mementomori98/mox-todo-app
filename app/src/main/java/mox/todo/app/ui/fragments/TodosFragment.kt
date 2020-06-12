package mox.todo.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mox.todo.app.R
import mox.todo.app.models.Todo
import mox.todo.app.ui.viewmodels.TodosViewModel

class TodosFragment(private val listId: Int? = null) : FragmentBase<TodosViewModel>() {

    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        root = getRoot(R.layout.fragment_todos, container, inflater)
        initViewModel<TodosViewModel>()
        viewModel.listId = listId

        setViewColors()
        setupRecyclerView()

        return root
    }

    private fun setupRecyclerView() {
        val recyclerView = root.findViewById<RecyclerView>(R.id.rv)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        TodosRecyclerAdapter(
            viewModel.todos(),
            { Toast.makeText(activity, "${it.title} selected", Toast.LENGTH_SHORT).show() },
            viewModel::deleteTodo,
            viewModel::addTodo,
            this::mapColor,
            recyclerView,
            resources,
            viewLifecycleOwner
        )
        resources
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
