package mox.todo.app.ui.fragments

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mox.todo.app.R
import java.util.function.Consumer

class TodosRecyclerViewHolder(itemView: View, listener: Consumer<Int>) : RecyclerView.ViewHolder(itemView) {

    val list: TextView = itemView.findViewById(R.id.list)
    val title: TextView = itemView.findViewById(R.id.title)
    val notes: TextView = itemView.findViewById(R.id.notes)
    val priority: TextView = itemView.findViewById(R.id.priority)
    val marker: View = itemView.findViewById(R.id.marker)

    init {
        itemView.setOnClickListener {
            listener.accept(adapterPosition)
        }

        hideEmptyText(notes)
        hideEmptyText(list)
    }

    private fun hideEmptyText(textView: TextView) {
        textView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if ((s?.toString() ?: "") == (""))
                    textView.height = 0
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
        })
    }
}