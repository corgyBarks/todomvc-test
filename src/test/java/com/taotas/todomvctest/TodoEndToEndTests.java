package com.taotas.todomvctest;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.taotas.todomvctest.utils.Action;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TodoEndToEndTests extends WithClearedStorageAfterEachTest {
    @Test
    public void todosLifeCycle() {

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

        add("a", "b", "c");

        toggle("b");
        todosShouldBe("a", "b", "c");

        filterActive();
        todosShouldBe("a", "c");

        filterCompleted();
        todosShouldBe("b");
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

    private void edit(String text, String editedText) {
        startEdit(text, editedText).pressEnter();
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
}
