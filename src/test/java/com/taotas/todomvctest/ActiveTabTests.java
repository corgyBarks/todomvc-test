package com.taotas.todomvctest;

import com.taotas.todomvctest.pages.TodoMvc;
import org.junit.jupiter.api.Test;

public class ActiveTabTests extends BaseTest{
    private TodoMvc todoMvc = new TodoMvc();

    @Test
    public void addTodo(){
        todoMvc.givenOpenedAtFilterActiveWith("a", "b", "c");

        todoMvc.add("added on active");

        todoMvc.todosShouldBe("a", "b", "c", "added on active");
        todoMvc.activeTodosShouldBe("a", "b", "c", "added on active");
        todoMvc.itemsLeftShouldBe(4);
    }

    @Test
    public void completeTodo() {
        todoMvc.givenOpenedAtFilterActiveWith("a", "b", "c");

        todoMvc.toggle("b");

        todoMvc.todosShouldBe("a", "c");
        todoMvc.activeTodosShouldBe("a","c");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    public void completeAll() {
        todoMvc.givenOpenedAtFilterActiveWith("a", "b", "c");

        todoMvc.toggleAll();

        todoMvc.todosShouldBeEmpty();
        todoMvc.activeTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(0);
    }

    @Test
    public void activateAll() {
        todoMvc.givenOpenedAtFilterActiveWith("a", "b", "c");
        todoMvc.toggleAll();

        todoMvc.toggleAll();
        todoMvc.todosShouldBe("a", "b", "c");
        todoMvc.activeTodosShouldBe("a", "b", "c");
    }

    @Test
    public void clearCompletedTodo() {
        todoMvc.givenOpenedWith("a", "b", "c");
        todoMvc.toggle("b");
        todoMvc.filterActive();

        todoMvc.clearCompleted();

        todoMvc.todosShouldBe("a", "c");
        todoMvc.activeTodosShouldBe("a", "c");
        todoMvc.itemsLeftShouldBe(2);
    }
}
