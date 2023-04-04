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
    public void addTodos() {
        givenAppOpened();
        verifyTodosEmpty();
        verifyFooterIsHidden();

        add("a");
        todosShouldBe("a");
        itemsLeftShouldBe(1);

        add("b", "c");
        todosShouldBe("a", "b", "c");
        itemsLeftShouldBe(3);
    }

    @Test
    public void editWithEnter() {
        givenAppOpenedWith("a", "b", "c");

        edit("b", "b edited");

        todosShouldBe("a", "b edited", "c");
        itemsLeftShouldBe(3);
    }

    @Test
    public void editWhenFocusChanged() {
        givenAppOpenedWith("a", "b", "c");

        editByTab("a", "a edited");

        todosShouldBe("a edited", "b", "c");
    }

    @Test
    public void cancelEditing() {
        givenAppOpenedWith("a", "b", "c");

        cancelEdit("b", " to be canceled");

        todosShouldBe("a", "b", "c");
    }

    @Test
    public void deleteByEditToEmpty() {
        givenAppOpenedWith("a", "b", "c");

        edit("b", "");

        todosShouldBe("a", "c");
    }

    @Test
    public void completeTodo() {
        givenAppOpenedWith("a", "b", "c");

        toggle("b");

        completedTodosShouldBe("b");
        activeTodosShouldBe("a", "c");
        itemsLeftShouldBe(2);
    }

    @Test
    public void completeAll() {
        givenAppOpenedWith("a", "b", "c");

        completeAllTodos();

        completedTodosShouldBe("a", "b", "c");
        activeTodosShouldBeEmpty();
        itemsLeftShouldBe(0);
    }

    @Test
    public void completeAllWithSomeCompleted() {
        givenAppOpenedWith("a", "b", "c");
        toggle("c");

        completeAllTodos();

        completedTodosShouldBe("a", "b", "c");
        activeTodosShouldBeEmpty();
        itemsLeftShouldBe(0);
    }

    @Test
    public void activateTodo() {
        givenAppOpenedWith("a", "b", "c");
        toggle("c");

        toggle("c");

        completedTodosShouldBeEmpty();
        activeTodosShouldBe("a", "b", "c");
    }

    @Test
    public void activateAll() {
        givenAppOpenedWith("a", "b", "c");
        completeAllTodos();

        completeAllTodos();

        completedTodosShouldBeEmpty();
        activeTodosShouldBe("a", "b", "c");
        itemsLeftShouldBe(3);
    }

    @Test
    public void deleteTodo() {
        givenAppOpenedWith("a", "b", "c");

        delete("b");
        todosShouldBe("a", "c");
        itemsLeftShouldBe(2);

        delete("a");
        delete("c");
        verifyTodosEmpty();
        verifyFooterIsHidden();
    }

    @Test
    public void clearCompletedTodo() {
        givenAppOpenedWith("a", "b", "c");
        toggle("b");

        clearCompleted();

        activeTodosShouldBe("a", "c");
        completedTodosShouldBeEmpty();
        itemsLeftShouldBe(2);
    }

    @Test
    public void clearCompletedAll() {
        givenAppOpenedWith("a", "b", "c");
        completeAllTodos();

        clearCompleted();

        verifyTodosEmpty();
        verifyFooterIsHidden();
    }

    private void completeAllTodos() {
        $("#toggle-all").click();
    }

    private void itemsLeftShouldBe(int counter) {
       // var text = (counter == 1) ? "1 item left" : String.format("%d items left", counter);
        $("#todo-count>strong").shouldHave(text(String.valueOf(counter)));
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

    private void activeTodosShouldBeEmpty() {
        todos.filterBy(Condition.cssClass("active")).shouldHave(size(0));
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

    private void verifyFooterIsHidden() {
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
