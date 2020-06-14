package mox.todo.app.ui.viewmodels;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import mox.todo.app.models.Todo;
import mox.todo.app.models.TodoList;
import mox.todo.app.repositories.TodoListRepository;
import mox.todo.app.util.LiveData;
import mox.todo.app.util.MutableLiveData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class MainViewModelTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    TodoListRepository listRepository;

    MainViewModel sut;

    @Before
    public void setup() {
        sut = new MainViewModel(listRepository);
    }

    @Test
    public void listIdNullOnInit() {
        assertNull(sut.getListId());
    }

    @Test
    public void todoListsReturnsRepoLiveData() {
        LiveData<List<TodoList>> liveData = new MutableLiveData<>();
        when(listRepository.liveData()).thenReturn(liveData);
        assertEquals(liveData, sut.todoLists());
    }

}
