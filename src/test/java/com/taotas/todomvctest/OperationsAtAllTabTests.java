package com.taotas.todomvctest;

import com.taotas.todomvctest.pages.TodoMvc;
import org.junit.jupiter.api.Test;

public class OperationsAtAllTabTests extends BaseTest {
    private TodoMvc todoMvc = new TodoMvc();

    @Test
    public void addTodos() {
        todoMvc.givenOpened();
        todoMvc.todosShouldBeEmpty();
        todoMvc.footerShouldBeHidden();

        todoMvc.add("a");

        todoMvc.todosShouldBe("a");
        todoMvc.itemsLeftShouldBe(1);

        todoMvc.add("b", "c");

        todoMvc.todosShouldBe("a", "b", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    public void editWithEnter() {
        todoMvc.givenOpenedWith("a", "b", "c");

        todoMvc.edit("b", "b edited");

        todoMvc.todosShouldBe("a", "b edited", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    public void editWithFocusChanged() {
        todoMvc.givenOpenedWith("a", "b", "c");

        todoMvc.editByTab("a", "a edited");

        todoMvc.todosShouldBe("a edited", "b", "c");
    }

    @Test
    public void cancelEditing() {
        todoMvc.givenOpenedWith("a", "b", "c");

        todoMvc.cancelEdit("b", " to be canceled");

        todoMvc.todosShouldBe("a", "b", "c");
    }

    @Test
    public void deleteByEditToEmpty() {
        todoMvc.givenOpenedWith("a", "b", "c");

        todoMvc.edit("b", "");

        todoMvc.todosShouldBe("a", "c");
    }

    @Test
    public void completeTodo() {
        todoMvc.givenOpenedWith("a", "b", "c");

        todoMvc.toggle("b");

        todoMvc.completedTodosShouldBe("b");
        todoMvc.activeTodosShouldBe("a", "c");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    public void completeAll() {
        todoMvc.givenOpenedWith("a", "b", "c");

        todoMvc.toggleAll();

        todoMvc.completedTodosShouldBe("a", "b", "c");
        todoMvc.activeTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(0);
    }

    @Test
    public void completeAllWithSomeCompleted() {
        todoMvc.givenOpenedWith("a", "b", "c");
        todoMvc.toggle("c");

        todoMvc.toggleAll();

        todoMvc.completedTodosShouldBe("a", "b", "c");
        todoMvc.activeTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(0);
    }

    @Test
    public void activateTodo() {
        todoMvc.givenOpenedWith("a", "b", "c");
        todoMvc.toggle("c");

        todoMvc.toggle("c");

        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.activeTodosShouldBe("a", "b", "c");
    }

    @Test
    public void activateAll() {
        todoMvc.givenOpenedWith("a", "b", "c");
        todoMvc.toggleAll();

        todoMvc.toggleAll();

        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.activeTodosShouldBe("a", "b", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    public void deleteTodo() {
        todoMvc.givenOpenedWith("a", "b", "c");

        todoMvc.delete("b");

        todoMvc.todosShouldBe("a", "c");
        todoMvc.itemsLeftShouldBe(2);

        todoMvc.delete("a");
        todoMvc.delete("c");

        todoMvc.todosShouldBeEmpty();
        todoMvc.footerShouldBeHidden();
    }

    @Test
    public void clearCompletedTodo() {
        todoMvc.givenOpenedWith("a", "b", "c");
        todoMvc.toggle("b");

        todoMvc.clearCompleted();

        todoMvc.activeTodosShouldBe("a", "c");
        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    public void clearCompletedAll() {
        todoMvc.givenOpenedWith("a", "b", "c");
        todoMvc.toggleAll();

        todoMvc.clearCompleted();

        todoMvc.todosShouldBeEmpty();
        todoMvc.footerShouldBeHidden();
    }
}
