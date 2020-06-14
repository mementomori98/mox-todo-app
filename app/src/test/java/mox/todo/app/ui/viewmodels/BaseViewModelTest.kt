package mox.todo.app.ui.viewmodels

import mox.todo.app.repositories.TodoListRepository
import mox.todo.app.repositories.TodoRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class BaseViewModelTest {

    @Rule @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var listRepository: TodoListRepository
    @Mock
    lateinit var todoRepository: TodoRepository

    lateinit var sut: BaseViewModel

    @Before
    fun setup() {
        sut = BaseViewModel(todoRepository, listRepository)
    }

    @Test
    fun updateLiveDatasCallsUpdateLiveDataInRepositories() {
        sut.updateLiveDatas()
        verify(todoRepository, times(1)).updateLiveData()
        verify(listRepository, times(1)).updateLiveData()
    }

}