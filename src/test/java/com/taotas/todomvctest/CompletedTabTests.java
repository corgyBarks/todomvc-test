package com.taotas.todomvctest;

import com.taotas.todomvctest.pages.TodoMvc;
import org.junit.jupiter.api.Test;

public class CompletedTabTests extends BaseTest{
    private TodoMvc todoMvc = new TodoMvc();

    @Test
    public void addTodo(){
        todoMvc.givenOpenedAtFilterCompletedWith("a", "b", "c");

        todoMvc.add("added on completed");

        todoMvc.visibleTodosShouldBeEmpty();
        todoMvc.completedTodosShouldBeEmpty();

        todoMvc.filterActive();

        todoMvc.activeTodosShouldBe("a", "b", "c", "added on completed");
        todoMvc.itemsLeftShouldBe(4);
    }

    @Test
    public void editCompleted()  {
        todoMvc.givenOpenedWith("a", "b", "c");
        todoMvc.toggle("b");
        todoMvc.filterCompleted();

        todoMvc.edit("b", "b edited");

        todoMvc.completedTodosShouldBe("b edited");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    public void deleteTodoWhenActiveExist() {
        todoMvc.givenOpenedWith("a", "b", "c");
        todoMvc.toggle("b");
        todoMvc.toggle("c");
        todoMvc.filterCompleted();

        todoMvc.delete("b");

        todoMvc.completedTodosShouldBe("c");
        todoMvc.itemsLeftShouldBe(1);
        todoMvc.verifyFooterIsVisible();
    }

    @Test
    public void deleteTodoWhenActiveEmpty() {
        todoMvc.givenOpenedWith( "b", "c");
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
        todoMvc.givenOpenedWith("a", "b", "c");
        todoMvc.toggle("c");
        todoMvc.filterCompleted();

        todoMvc.toggle("c");

        todoMvc.visibleTodosShouldBeEmpty();
        todoMvc.completedTodosShouldBeEmpty();
    }

    @Test
    public void activateAll(){
        todoMvc.givenOpenedWith("a", "b", "c");
        todoMvc.toggleAll();
        todoMvc.filterCompleted();

        todoMvc.toggleAll();

        todoMvc.visibleTodosShouldBeEmpty();
        todoMvc.completedTodosShouldBeEmpty();
    }

    @Test
    public void completeAll(){
        todoMvc.givenOpenedAtFilterCompletedWith("a", "b", "c");

        todoMvc.toggleAll();

        todoMvc.visibleTodosShouldBe("a", "b", "c");
        todoMvc.completedTodosShouldBe("a", "b", "c");
    }

    @Test
    public void clearCompletedTodo() {
        todoMvc.givenOpenedWith("a", "b", "c");
        todoMvc.toggle("b");
        todoMvc.filterCompleted();

        todoMvc.clearCompleted();

        todoMvc.visibleTodosShouldBeEmpty();
        todoMvc.completedTodosShouldBeEmpty();

        todoMvc.filterActive();

        todoMvc.activeTodosShouldBe("a", "c");
        todoMvc.itemsLeftShouldBe(2);
    }
}
