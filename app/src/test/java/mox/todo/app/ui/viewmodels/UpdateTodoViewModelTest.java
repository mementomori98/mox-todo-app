package mox.todo.app.ui.viewmodels;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import mox.todo.app.models.Todo;
import mox.todo.app.repositories.TodoListRepository;
import mox.todo.app.repositories.TodoRepository;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static kotlin.text.Typography.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateTodoViewModelTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    TodoRepository todoRepository;

    @Mock
    TodoListRepository listRepository;

    UpdateTodoViewModel sut;

    @Before
    public void setup() {
        sut = new UpdateTodoViewModel(todoRepository, listRepository);
    }

    @Test
    public void updateTodoCallsRepoWithSameParam() {
        Todo todo = new Todo("", "", 0, "", 0, 0, 0);
        sut.updateTodo(todo);
        verify(todoRepository, times(1)).update(todo);
    }

    @Test
    public void updateTodoReturnsTrueWhenRepoReturnsTrue() {
        when(todoRepository.update(any())).thenReturn(true);
        assertTrue(sut.updateTodo(new Todo("", "", 0, "", 0, 0, 0)));
    }

    @Test
    public void updateTodoReturnsFalseWhenRepoReturnsFalse() {
        assertFalse(sut.updateTodo(new Todo("", "", 0, "", 0, 0, 0)));
    }

}
