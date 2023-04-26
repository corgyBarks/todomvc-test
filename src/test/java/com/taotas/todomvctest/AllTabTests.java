package com.taotas.todomvctest;

import com.taotas.todomvctest.pages.TodoMvcPage;
import org.junit.jupiter.api.Test;

public class AllTabTests extends BaseTest {
    private TodoMvcPage todoMvc = new TodoMvcPage();
    @Test
    public void addTodos() {
        todoMvc.givenAppOpened();
        todoMvc.verifyTodosEmpty();
        todoMvc.verifyFooterIsHidden();

        todoMvc.add("a");
        todoMvc.todosShouldBe("a");
        todoMvc.itemsLeftShouldBe(1);

        todoMvc.add("b", "c");
        todoMvc.todosShouldBe("a", "b", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    public void editWithEnter() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.edit("b", "b edited");

        todoMvc.todosShouldBe("a", "b edited", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    public void editWhenFocusChanged() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.editByTab("a", "a edited");

        todoMvc.todosShouldBe("a edited", "b", "c");
    }

    @Test
    public void cancelEditing() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.cancelEdit("b", " to be canceled");

        todoMvc.todosShouldBe("a", "b", "c");
    }

    @Test
    public void deleteByEditToEmpty() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.edit("b", "");

        todoMvc.todosShouldBe("a", "c");
    }

    @Test
    public void completeTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.toggle("b");

        todoMvc.completedTodosShouldBe("b");
        todoMvc.activeTodosShouldBe("a", "c");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    public void completeAll() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.toggleAll();

        todoMvc.completedTodosShouldBe("a", "b", "c");
        todoMvc.activeTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(0);
    }

    @Test
    public void completeAllWithSomeCompleted() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("c");

        todoMvc.toggleAll();

        todoMvc.completedTodosShouldBe("a", "b", "c");
        todoMvc.activeTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(0);
    }

    @Test
    public void activateTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("c");

        todoMvc.toggle("c");

        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.activeTodosShouldBe("a", "b", "c");
    }

    @Test
    public void activateAll() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggleAll();

        todoMvc.toggleAll();

        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.activeTodosShouldBe("a", "b", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    public void deleteTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.delete("b");
        todoMvc.todosShouldBe("a", "c");
        todoMvc.itemsLeftShouldBe(2);

        todoMvc.delete("a");
        todoMvc.delete("c");
        todoMvc.verifyTodosEmpty();
        todoMvc.verifyFooterIsHidden();
    }

    @Test
    public void clearCompletedTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("b");

        todoMvc.clearCompleted();

        todoMvc.activeTodosShouldBe("a", "c");
        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    public void clearCompletedAll() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggleAll();

        todoMvc.clearCompleted();

        todoMvc.verifyTodosEmpty();
        todoMvc.verifyFooterIsHidden();
    }
}
