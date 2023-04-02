package com.taotas.todomvctest;

import com.codeborne.selenide.*;
import com.taotas.todomvctest.utils.Action;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TodoEndToEndTests extends BaseTest {

    @Test
    public void todosLifeCycle() {
        givenAppOpened();

        add("a", "b", "c");
        todosShouldBe("a", "b", "c");

        edit("b", "b edited");

        toggle("b edited");
        clearCompleted();
        todosShouldBe("a", "c");

        cancelEdit("c", " to be canceled");

        delete("c");
        todosShouldBe("a");
    }

    @Test
    public void filtersTasks() {
        givenAppOpenedWith("a", "b", "c");
        toggle("b");

        filterActive();
        todosShouldBe("a", "c");

        filterCompleted();
        todosShouldBe("b");

        filterAll();
        todosShouldBe("a", "b", "c");
    }

    @Test
    public void testTodosEmptyWhenOpening() {
        givenAppOpened();

        verifyTodosEmpty();
    }

    @Test
    public void testAddingTodos() {
        givenAppOpened();

        add("a");
        todosShouldBe("a");
        itemsLeftShouldBe(1);

        add("b");
        todosShouldBe("a", "b");
        itemsLeftShouldBe(2);

        add("c");
        todosShouldBe("a", "b", "c");
        itemsLeftShouldBe(3);
    }

    @Test
    public void testEditWithEnter() {
        givenAppOpenedWith("a", "b", "c");

        edit("b", "b edited");
        todosShouldBe("a", "b edited", "c");
        itemsLeftShouldBe(3);
    }

    @Test
    public void testEditWithTab() {
        givenAppOpenedWith("a", "b", "c");

        editByTab("b", "b edited");
        todosShouldBe("a", "b edited", "c");
        itemsLeftShouldBe(3);
    }

    @Test
    public void testEditCompleted() {
        givenAppOpenedWith("a", "b", "c");
        toggle("b");

        edit("b", "b edited");
        todosShouldBe("a", "b edited", "c");
        itemsLeftShouldBe(2);
    }

    @Test
    public void testDeleteTodoByChangeTextToEmpty() {
        givenAppOpenedWith("a", "b", "c");

        edit("b", "");
        todosShouldBe("a", "c");
    }

    @Test
    public void testCompletingTodo() {
        givenAppOpenedWith("a", "b", "c");

        itemsLeftShouldBe(3);
        toggle("b");
        itemsLeftShouldBe(2);
        completedTodosShouldBe("b");
        todosShouldBe("a", "b", "c");
    }

    @Test
    public void testCompletingAllTodos() {
        givenAppOpenedWith("a", "b", "c");

        completeAllTodos();
        clearCompleted();
        verifyTodosEmpty();
    }

    @Test
    public void testUncompletingTodo() {
        givenAppOpenedWith("a", "b", "c");

        itemsLeftShouldBe(3);
        toggle("b");
        completedTodosShouldBe("b");
        toggle("b");
        completedTodosShouldBeEmpty();
    }


    @Test
    public void testCancellingOfEditingTodo() {
        givenAppOpenedWith("a", "b", "c");

        cancelEdit("b", " to be canceled");
        todosShouldBe("a", "b", "c");
    }

    @Test
    public void testDeletingTodo() {
        givenAppOpenedWith("a", "b", "c");

        itemsLeftShouldBe(3);
        delete("b");
        todosShouldBe("a", "c");
        itemsLeftShouldBe(2);
    }

    @Test
    public void testDeletingOnlyOneAddedTodo() {
        givenAppOpenedWith("a");

        delete("a");
        verifyTodosEmpty();
    }

    @Test
    public void testClearingCompletedTodos() {
        givenAppOpenedWith("a", "b", "c");
        toggle("b");

        clearCompleted();
        todosShouldBe("a", "c");
        completeAllTodos();
        clearCompleted();
        verifyTodosEmpty();
    }

    private void completeAllTodos() {
        $("#toggle-all").click();
    }

    private void itemsLeftShouldBe(int counter) {
        var c = (counter == 1) ? "1 item left" : String.format("%d items left", counter);
        $("#todo-count").shouldHave(text(c));
    }

    private void verifyTodosEmpty() {
        todos.shouldHave(size(0));
    }

    private void givenAppOpened() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.clearBrowserLocalStorage();
        }
        Selenide.open("/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return Object.keys(require.s.contexts._.defined).length === 39;"));
    }

    public void givenAppOpenedWith(String... texts) {
        givenAppOpened();
        add(texts);
    }

    public static final ElementsCollection todos = $$("#todo-list li");

    private void add(String... texts) {
        for (String text : texts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    private void todosShouldBe(String... todoTexts) {
        todos.filter(visible).shouldHave(exactTexts(todoTexts));
    }

    private void completedTodosShouldBe(String... todoTexts) {
        todos.filterBy(Condition.cssClass("completed")).shouldHave(exactTexts(todoTexts));
    }

    private void completedTodosShouldBeEmpty() {
        todos.filterBy(Condition.cssClass("completed")).shouldHave(size(0));
    }

    private void edit(String text, String editedText) {
        startEdit(text, editedText).pressEnter();
    }

    private void editByTab(String text, String editedText) {
        startEdit(text, editedText).pressTab();
    }

    private void toggle(String text) {
        todos.findBy(exactText(text)).find(".toggle").click();

    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void cancelEdit(String text, String editedText) {
        startEdit(text, editedText).pressEscape();
    }

    private void delete(String text) {
        todos.findBy(exactText(text)).hover()
                .find(".destroy").click();
    }

    private SelenideElement startEdit(String text, String newText) {
        todos.findBy(exactText(text)).doubleClick();
        return todos.findBy(cssClass("editing"))
                .find(".edit")
                .execute(Action.JS.setValue(newText));
    }

    private void filterActive() {
        $("[href*='#/active']").click();
    }

    private void filterCompleted() {
        $("[href*='#/completed']").click();
    }

    private void filterAll() {
        $("[href*='#/']").click();
    }
}
