package mox.todo.app.ui.viewmodels;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mox.todo.app.models.Todo;
import mox.todo.app.models.TodoList;
import mox.todo.app.repositories.TodoListRepository;
import mox.todo.app.repositories.TodoRepository;
import mox.todo.app.util.LiveData;
import mox.todo.app.util.MutableLiveData;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TodoViewModelTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    TodoRepository todoRepository;

    @Mock
    TodoListRepository listRepository;

    TodosViewModel sut;

    @Before
    public void setup() {
        sut = new TodosViewModel(todoRepository, listRepository);
    }

    @Test
    public void hasListsReturnsTrueWhenRepoGetAllNotEmpty() {
        when(listRepository.getAll()).thenReturn(Arrays.asList(new TodoList("", 0, 0)));
        assertTrue(sut.hasLists());
    }

    @Test
    public void hasListsReturnsFalseWhenRepoGetAllEmpty() {
        when(listRepository.getAll()).thenReturn(new ArrayList<>());
        assertFalse(sut.hasLists());
    }

    @Test
    public void todosCallsLiveDataWithNullWhenListIdNull() {
        sut.todos();
        verify(todoRepository, times(1)).liveData(null);
    }

    @Test
    public void todosCallsLiveDataWithListNameWhenListIdNotNull() {
        sut.setListId(1);
        when(listRepository.getById(1)).thenReturn(new TodoList("value", 0, 0));
        sut.todos();

        verify(todoRepository, times(1)).liveData("value");
    }

    @Test
    public void todosReturnsWhatRepositoryReturns() {
        LiveData<List<Todo>> liveData = new MutableLiveData<>();
        when(todoRepository.liveData(any())).thenReturn(liveData);
        assertEquals(liveData, sut.todos());
    }

    @Test
    public void listNameReturns_AllTodos_WhenListIdNull() {
        assertEquals("All Todos", sut.listName());
    }

    @Test
    public void listNameReturnsNameWhatListRepoReturns() {
        sut.setListId(1);
        when(listRepository.getById(1)).thenReturn(new TodoList("Name", 0, 0));
        assertEquals("Name", sut.listName());
    }
    
    @Test
    public void listNameReturns_AllTodos_WhenRepoReturnsNull() {
        sut.setListId(1);
        assertEquals("All Todos", sut.listName());
    }

    @Test
    public void listColorReturns0WhenListIdNull() {
        assertEquals(0, sut.listColor());
    }

    @Test
    public void listColorReturnsColorOfListReturnedByRepo() {
        sut.setListId(1);
        when(listRepository.getById(1)).thenReturn(new TodoList("name", 2, 0));
        assertEquals(2, sut.listColor());
    }

    @Test
    public void listColor0WhenRepoReturnsNull() {
        sut.setListId(1);
        assertEquals(0, sut.listColor());
    }

    @Test
    public void deleteTodoCallsRepo() {
        sut.deleteTodo(new Todo("name", "", 0, "", 0, 2, 0));
        verify(todoRepository, times(1)).delete(2);
        verify(todoRepository, times(1)).delete(anyInt());
    }

    @Test
    public void deleteListCallsRepo() {
        sut.setListId(1);
        sut.deleteList();
        verify(listRepository, times(1)).delete(1);
        verify(listRepository, times(1)).delete(anyInt());
    }

    @Test(expected = Exception.class)
    public void deleteListThrowsExceptionOnNullListId() {
        sut.deleteList();
    }

    @Test
    public void addTodoCallsRepoWithSameParams() {
        Todo todo = new Todo("", "", 0, "", 0, 0, 0);
        sut.addTodo(todo, 0);
        verify(todoRepository, times(1)).add(todo, 0);
        verify(todoRepository, times(1)).add(any(), anyInt());
    }

    @Test
    public void addTodoReturnsFalseWhenRepoReturnsFalse() {
        assertFalse(sut.addTodo(new Todo("", "", 0, "", 0, 0, 0), 0));
    }

    @Test
    public void addTodoReturnsTrueWhenRepoReturnsTrue() {
        when(todoRepository.add(any(), anyInt())).thenReturn(true);
        assertTrue(sut.addTodo(new Todo("", "", 0, "", 0, 0, 0), 0));
    }

}
