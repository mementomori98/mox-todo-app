package mox.todo.app.models

data class Todo(
    val title: String = "",
    val notes: String = "",
    val priority: Int = 1,
    val list: String? = null,
    val color: Int = 0,
    val key: Int = 0
)