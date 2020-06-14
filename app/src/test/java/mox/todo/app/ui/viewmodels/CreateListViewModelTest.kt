package mox.todo.app.ui.viewmodels

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import mox.todo.app.models.TodoList
import mox.todo.app.repositories.TodoListRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as setup
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class CreateListViewModelTest {

    private fun <T> all(): T = Mockito.any()

    @Rule @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var listRepository: TodoListRepository

    lateinit var sut: CreateListViewModel

    @Before
    fun setup() {
        sut = CreateListViewModel(listRepository)
    }

    @Test
    fun addTodoListReturnsFalseWhenRepoReturnsFalse() {
        val list = TodoList("name", 0, 0)
        setup(listRepository.add(all())).thenReturn(true)
        setup(listRepository.add(list)).thenReturn(false)
        val result = sut.addTodoList(list)
        assertFalse(result)
    }

    @Test
    fun addTodoListReturnsTrueWhenRepoReturnsTrue() {
        val list = TodoList("name", 0, 0)
        setup(listRepository.add(list)).thenReturn(true)
        val result = sut.addTodoList(list)
        assertTrue(result)
    }

    @Test
    fun addTodoListCallsRepositoryOnce() {
        val list = TodoList("name")
        sut.addTodoList(list)
        verify(listRepository, times(1)).add(list)
        verify(listRepository, times(1)).add(all())
    }

}