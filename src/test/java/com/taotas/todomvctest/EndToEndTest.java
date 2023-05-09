package com.taotas.todomvctest;

import com.taotas.todomvctest.pages.TodoMvc;
import org.junit.jupiter.api.Test;

public class EndToEndTest extends BaseTest{
    private TodoMvc todoMvc = new TodoMvc();

    @Test
    public void todosLifeCycle() {
        todoMvc.givenOpened();
        todoMvc.add("a", "b", "c");

        todoMvc.todosShouldBe("a", "b", "c");

        todoMvc.edit("b", "b edited");
        todoMvc.toggle("b edited");
        todoMvc.clearCompleted();

        todoMvc.todosShouldBe("a", "c");

        todoMvc.cancelEdit("c", " to be canceled");
        todoMvc.delete("c");

        todoMvc.todosShouldBe("a");
    }

    @Test
    public void filtersTasks() {
        todoMvc.givenOpenedWith("a", "b", "c");
        todoMvc.toggle("b");

        todoMvc.filterActive();
        todoMvc.todosShouldBe("a", "c");

        todoMvc.filterCompleted();
        todoMvc.todosShouldBe("b");

        todoMvc.filterAll();
        todoMvc.todosShouldBe("a", "b", "c");
    }

}
