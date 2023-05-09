package com.taotas.todomvctest;

import com.taotas.todomvctest.pages.TodoMvc;
import org.junit.jupiter.api.Test;

public class EndToEndTest extends BaseTest{
    private TodoMvc todoMvc = new TodoMvc();

    @Test
    public void todosLifeCycle() {
        todoMvc.givenOpened();
        todoMvc.add("a", "b", "c");

        todoMvc.visibleTodosShouldBe("a", "b", "c");

        todoMvc.edit("b", "b edited");
        todoMvc.toggle("b edited");
        todoMvc.clearCompleted();

        todoMvc.visibleTodosShouldBe("a", "c");

        todoMvc.cancelEdit("c", " to be canceled");
        todoMvc.delete("c");

        todoMvc.visibleTodosShouldBe("a");
    }

    @Test
    public void filtersTasks() {
        todoMvc.givenOpenedWith("a", "b", "c");
        todoMvc.toggle("b");

        todoMvc.filterActive();
        todoMvc.visibleTodosShouldBe("a", "c");

        todoMvc.filterCompleted();
        todoMvc.visibleTodosShouldBe("b");

        todoMvc.filterAll();
        todoMvc.visibleTodosShouldBe("a", "b", "c");
    }

}
