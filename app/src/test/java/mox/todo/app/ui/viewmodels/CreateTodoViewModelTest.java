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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateTodoViewModelTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    TodoRepository todoRepository;

    @Mock
    TodoListRepository listRepository;

    CreateTodoViewModel sut;

    @Before
    public void setup() {
        sut = new CreateTodoViewModel(todoRepository, listRepository);
    }

    @Test
    public void listIdNullOnSetup() {
        assertNull(sut.getListId());
    }

    @Test
    public void listNameEmptyStringWhenListIdNull() {
        assertEquals("", sut.getListName());
    }
    
    @Test
    public void listNameCallRepositoryWhenListIdNotNull() {
        sut.setListId(100);
        sut.getListName();
        verify(listRepository, times(1)).getById(100);
        verify(listRepository, times(1)).getById(anyInt());
    }

    @Test
    public void listNameEmptyWhenRepositoryReturnsNull() {
        sut.setListId(100);
        assertEquals("", sut.getListName());
    }

    @Test
    public void listNameSameAsReturnedList() {
        sut.setListId(100);
        when(listRepository.getById(anyInt())).thenReturn(new TodoList("valid", 0, 0));
        assertEquals("valid", sut.getListName());
    }

    @Test
    public void addTodoCallsRepository() {
        Todo todo = new Todo("", "", 0, "", 0, 0, 0);
        sut.addTodo(todo);
        verify(todoRepository, times(1)).add(todo, 0);
    }

    @Test
    public void addReturnsTrueIfRepoReturnsTrue() {
        when(todoRepository.add(any(), anyInt())).thenReturn(true);
        assertTrue(sut.addTodo(new Todo("", "", 0, "", 0, 0, 0)));
    }

    @Test
    public void addReturnsFalseIfRepoReturnsFalse() {
        assertFalse(sut.addTodo(new Todo("", "", 0, "", 0, 0, 0)));
    }

    @Test
    public void listNamesReturnsNamesMapped() {
        when(listRepository.getAll()).thenReturn(Arrays.asList(
                new TodoList("name1", 0, 0),
                new TodoList("name2", 0, 0)
        ));
        List<String> result = sut.listNames();
        assertEquals("name1", result.get(0));
        assertEquals("name2", result.get(1));
        assertEquals(2, result.size());
    }

    @Test
    public void listNameReturnsEmptyListWhenRepoReturnsEmptyList() {
        when(listRepository.getAll()).thenReturn(new ArrayList<>());
        assertEquals(0, sut.listNames().size());
    }

}
