package mox.todo.app.models

import java.io.Serializable

data class TodoList(
    var name: String,
    var color: Int = 0,
    var id: Int = 0
) : Serializable