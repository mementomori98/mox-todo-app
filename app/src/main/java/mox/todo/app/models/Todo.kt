package mox.todo.app.models

data class Todo(
    var title: String,
    var notes: String,
    var priority: Int,
    var listId: Int? = null,
    var id: Int = 0
)