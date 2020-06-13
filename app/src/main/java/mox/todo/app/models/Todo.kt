package mox.todo.app.models

import java.io.Serializable

data class Todo(
    val title: String = "",
    val notes: String = "",
    val priority: Int = 1,
    val list: String? = null,
    val color: Int = 0,
    val key: Int = 0,
    var position: Int = 0
): Serializable