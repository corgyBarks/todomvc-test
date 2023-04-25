package com.taotas.todomvctest;

import com.taotas.todomvctest.pages.TodoMvcPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.Sleeper;

public class CompletedEndToEndTests extends BaseTest{
    private TodoMvcPage todoMvc = new TodoMvcPage();

    @Test
    public void addTodo(){
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.filterCompleted();

        todoMvc.add("added on completed");
        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.filterActive();
        todoMvc.activeTodosShouldBe("a", "b", "c", "added on completed");
        todoMvc.itemsLeftShouldBe(4);
    }

    @Test
    public void editCompleted()  {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("b");
        todoMvc.filterCompleted();

        todoMvc.edit("b", "b edited");
        todoMvc.completedTodosShouldBe("b edited");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    public void deleteTodoWhenActiveExist() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("b");
        todoMvc.toggle("c");
        todoMvc.filterCompleted();

        todoMvc.delete("b");
        todoMvc.completedTodosShouldBe("c");
        todoMvc.itemsLeftShouldBe(1);

        todoMvc.delete("c");
        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.verifyFooterIsVisible();
    }

    @Test
    public void deleteTodoWhenActiveEmpty() {
        todoMvc.givenAppOpenedWith( "b", "c");
        todoMvc.toggle("b");
        todoMvc.toggle("c");
        todoMvc.filterCompleted();

        todoMvc.delete("b");
        todoMvc.completedTodosShouldBe("c");

        todoMvc.delete("c");
        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.verifyFooterIsHidden();
    }

    @Test
    public void activateTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("c");
        todoMvc.filterCompleted();

        todoMvc.toggle("c");

        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.filterActive();
        todoMvc.activeTodosShouldBe("a", "b", "c");
    }

    @Test
    public void clearCompletedTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("b");
        todoMvc.filterCompleted();

        todoMvc.clearCompleted();

        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.filterActive();
        todoMvc.activeTodosShouldBe("a", "c");
        todoMvc.itemsLeftShouldBe(2);
    }
}
