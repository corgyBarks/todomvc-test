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
    public void testTodosEmptyOnFreshOpen() {
        givenAppOpened();

        verifyTodosEmpty();
        footerIsHidden();
    }

    @Test
    public void testAddTodos() {
        givenAppOpened();

        add("a");
        todosShouldBe("a");
        itemsLeftShouldBe(1);
        footerIsVisible();

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
    public void testCancelEditingTodo() {
        givenAppOpenedWith("a", "b", "c");

        cancelEdit("b", " to be canceled");
        todosShouldBe("a", "b", "c");
    }

    @Test
    public void testDeleteTodoByChangeTextToEmpty() {
        givenAppOpenedWith("a", "b", "c");

        edit("b", "");

        todosShouldBe("a", "c");
    }

    @Test
    public void testCompleteTodo() {
        givenAppOpenedWith("a", "b", "c");

        itemsLeftShouldBe(3);
        toggle("b");
        itemsLeftShouldBe(2);
        completedTodosShouldBe("b");
        activeTodosShouldBe("a", "c");
    }

    @Test
    public void testCompleteAllTodos() {
        givenAppOpenedWith("a", "b", "c");

        completeAllTodos();
        clearCompleted();
        verifyTodosEmpty();
        footerIsHidden();
    }

    @Test
    public void testActivateTodo() {
        givenAppOpenedWith("a", "b", "c");

        itemsLeftShouldBe(3);
        toggle("b");
        completedTodosShouldBe("b");
        toggle("b");
        completedTodosShouldBeEmpty();
    }


    @Test
    public void testDeleteTodo() {
        givenAppOpenedWith("a", "b", "c");

        itemsLeftShouldBe(3);
        delete("b");
        todosShouldBe("a", "c");
        itemsLeftShouldBe(2);
    }

    @Test
    public void testDeleteLastTodo() {
        givenAppOpenedWith("a");

        delete("a");
        verifyTodosEmpty();
        footerIsHidden();
    }

    @Test
    public void testClearCompletedTodos() {
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

    private void activeTodosShouldBe(String... todoTexts) {
        todos.filterBy(Condition.cssClass("active")).shouldHave(exactTexts(todoTexts));
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
    private void footerIsVisible() {
        $("#footer").shouldBe(visible);
    }
    private void footerIsHidden() {
        $("#footer").shouldBe(hidden);
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
