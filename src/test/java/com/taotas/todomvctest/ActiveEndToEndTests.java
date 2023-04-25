package com.taotas.todomvctest;

import com.taotas.todomvctest.pages.TodoMvcPage;
import org.junit.jupiter.api.Test;

public class ActiveEndToEndTests extends BaseTest{
    private TodoMvcPage todoMvc = new TodoMvcPage();

    @Test
    public void addTodo(){
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.filterActive();

        todoMvc.add("added on active");
        todoMvc.activeTodosShouldBe("a", "b", "c", "added on active");
        todoMvc.itemsLeftShouldBe(4);
    }

    @Test
    public void completeTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.filterActive();

        todoMvc.toggle("b");

        todoMvc.activeTodosShouldBe("a", "c");
        todoMvc.filterCompleted();
        todoMvc.completedTodosShouldBe("b");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    public void completeAll() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.filterActive();

        todoMvc.toggleAll();

        todoMvc.activeTodosShouldBeEmpty();
        todoMvc.filterCompleted();
        todoMvc.completedTodosShouldBe("a", "b", "c");
        todoMvc.itemsLeftShouldBe(0);
    }

    @Test
    public void clearCompletedTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("b");
        todoMvc.filterActive();

        todoMvc.clearCompleted();

        todoMvc.activeTodosShouldBe("a", "c");
        todoMvc.filterCompleted();
        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(2);
    }
}
