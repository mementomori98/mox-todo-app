package mox.todo.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import mox.todo.app.repositories.TodoApiRepository
import mox.todo.app.repositories.TodoListApiRepository

class StartupViewModel : ViewModel() {

    private val todoRepository = TodoApiRepository.instance
    private val listRepository = TodoListApiRepository.instance

    fun updateLiveDatas() {
        todoRepository.updateLiveData()
        listRepository.updateLiveData()
    }

}