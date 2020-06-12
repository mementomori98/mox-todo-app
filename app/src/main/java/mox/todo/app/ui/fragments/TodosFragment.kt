package mox.todo.app.ui.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import mox.todo.app.util.LiveData
import mox.todo.app.R
import mox.todo.app.models.Todo
import mox.todo.app.ui.viewmodels.TodosViewModel
import java.util.function.Consumer

class TodosFragment(private val listId: Int? = null) : FragmentBase<TodosViewModel>() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel<TodosViewModel>()
        viewModel.listId = listId

        adapter = Adapter(viewModel.todos())
        viewModel.todos()
            .observe(viewLifecycleOwner, Observer { adapter.notifyDataSetChanged() })
        activity?.title = viewModel.listName()
        activity?.findViewById<Toolbar>(R.id.toolbar)
            ?.setBackgroundColor(
                resources.getColor(mapListColor(viewModel.listColor()), null))
        activity?.findViewById<View>(R.id.drawer)
            ?.setBackgroundColor(
                resources.getColor(mapListColor(viewModel.listColor()), null)
            )

        val root = getRoot(R.layout.fragment_todos, container, inflater)
        recyclerView = root.findViewById(R.id.rv)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.adapter = adapter

        ItemTouchHelper(touchListener()).attachToRecyclerView(recyclerView)

        return root
    }

    private fun mapListColor(listColor: Int): Int {
        return when(listColor) {
            0 -> R.color.blue
            1 -> R.color.red
            2 -> R.color.orange
            3 -> R.color.green
            else -> R.id.blue
        }
    }

    private fun touchListener() = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val todo = viewModel.todos().value.filter {
                (it.list ?: "All Todos").let {
                        name -> viewModel.listName() == name
                } || viewModel.listId == null
            }[position]
            viewModel.deleteTodo(todo)
            Snackbar.make(recyclerView, "Deleted '${todo.title}'", Snackbar.LENGTH_LONG)
                .setAction("UNDO") { viewModel.addTodo(todo, position) }
                .setTextColor(resources.getColor(R.color.textColorDark))
                .show()
        }

    }

    inner class Adapter(private val todos: LiveData<List<Todo>>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        private val data get() = todos.value.filter {
            (it.list ?: "All Todos").let {
                    name -> viewModel.listName() == name
            } || viewModel.listId == null
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.rv_todo_item, parent, false)

            return ViewHolder(view, Consumer {
                Toast.makeText(parent.context, todos.value[it].title, Toast.LENGTH_SHORT).show()
            })
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val todo = data[position]
            holder.title.text = todo.title
            holder.notes.text = todo.notes
            holder.priority.text = todo.priority.toString()
            holder.list.text = todo.list
            holder.marker.setBackgroundColor(resources.getColor(mapListColor(todo.color), null))
        }

        inner class ViewHolder(itemView: View, listener: Consumer<Int>) : RecyclerView.ViewHolder(itemView) {

            val list: TextView = itemView.findViewById(R.id.list)
            val title: TextView = itemView.findViewById(R.id.title)
            val notes: TextView = itemView.findViewById(R.id.notes)
            val priority: TextView = itemView.findViewById(R.id.priority)
            val marker: View = itemView.findViewById(R.id.marker)

            init {
                itemView.setOnClickListener {
                    listener.accept(adapterPosition)
                }

                notes.addTextChangedListener(textViewHider(notes))
                list.addTextChangedListener(textViewHider(list))
            }

            private fun textViewHider(tv: TextView) = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if ((s?.toString() ?: "") == (""))
                        tv.height = 0
                    else {
                        val params = notes.layoutParams
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        notes.layoutParams = params
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            }
        }
    }

}
