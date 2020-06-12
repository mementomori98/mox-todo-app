package mox.todo.app.ui.fragments

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import mox.todo.app.R
import mox.todo.app.models.Todo
import mox.todo.app.util.LiveData
import java.util.function.Consumer

class TodosRecyclerAdapter(
    private val todos: LiveData<List<Todo>>,
    private val selectionListener: (Todo) -> Unit,
    private val deletionListener: (Todo) -> Unit,
    private val additionListener: (Todo, Int) -> Unit,
    private val colorMapper: (Int) -> Int,
    private val recyclerView: RecyclerView,
    private val resources: Resources,
    lifeCycleOwner: LifecycleOwner
) : RecyclerView.Adapter<TodosRecyclerViewHolder>() {

    init {
        todos.observe(lifeCycleOwner, Observer {
            notifyDataSetChanged()
        })

        recyclerView.adapter = this
        ItemTouchHelper(touchListener()).attachToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodosRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_todo_item, parent, false)

        return TodosRecyclerViewHolder(view, Consumer { selectionListener(todos.value[it]) })
    }

    override fun getItemCount(): Int {
        return todos.value.size
    }

    override fun onBindViewHolder(holder: TodosRecyclerViewHolder, position: Int) {
        val todo = todos.value[position]
        holder.title.text = todo.title
        holder.notes.text = todo.notes
        holder.priority.text = todo.priority.toString()
        holder.list.text = todo.list
        holder.marker.setBackgroundColor(resources.getColor(colorMapper(todo.color), null))
    }

    private fun touchListener() = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val todo = todos.value[position]
            deletionListener(todo)
            Snackbar.make(recyclerView, "Deleted '${todo.title}'", Snackbar.LENGTH_LONG)
                .setAction("UNDO") { additionListener(todo, position) }
                .setTextColor(resources.getColor(R.color.textColorDark, null))
                .show()
        }

    }
}