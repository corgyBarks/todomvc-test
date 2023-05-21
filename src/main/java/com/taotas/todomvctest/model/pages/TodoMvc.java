package com.taotas.todomvctest.model.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.taotas.todomvctest.model.controls.EditableLabelItem;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TodoMvc{
    public static final ElementsCollection todos = $$("#todo-list > li");

    private static final ElementsCollection activeTodos = todos.filterBy(Condition.cssClass("active"));
    private static final ElementsCollection completedTodos = todos.filterBy(Condition.cssClass("completed"));

    public void givenOpened() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.clearBrowserLocalStorage();
        }
        Selenide.open("/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return Object.keys(require.s.contexts._.defined).length === 39;"));
    }

    public void givenOpenedWith(String... texts) {
        givenOpened();
        add(texts);
    }

    public void givenOpenedAtFilterActiveWith(String... todoTexts) {
        givenOpenedWith(todoTexts);
        filterActive();
    }

    public void givenOpenedAtFilterCompletedWith(String... todoTexts) {
        givenOpenedWith(todoTexts);
        filterCompleted();
    }

    public void toggleAll() {
        $("#toggle-all").click();
    }

    public void itemsLeftShouldBe(int counter) {
        $("#todo-count>strong").shouldHave(text(String.valueOf(counter)));
    }

    public void todosShouldBeEmpty() {
        todos.filter(visible).shouldHave(size(0));
    }

    public void add(String... texts) {
        for (String text : texts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    public void edit(String oldText, String newText) {
        editableLabel(oldText).edit(newText);
    }
    public void editByTab(String oldText, String newText) {
        editableLabel(oldText).editByTab(newText);
    }
    public void cancelEdit(String oldText, String newText) {
        editableLabel(oldText).cancelEdit(newText);
    }

    public void todosShouldBe(String... todoTexts) {
        todos.filter(visible).shouldHave(exactTexts(todoTexts));
    }

    public void completedTodosShouldBe(String... todoTexts) {
        completedTodos.shouldHave(exactTexts(todoTexts));
    }

    public void activeTodosShouldBe(String... todoTexts) {
        activeTodos.shouldHave(exactTexts(todoTexts));
    }

    public void completedTodosShouldBeEmpty() {
        completedTodos.shouldHave(size(0));
    }

    public void activeTodosShouldBeEmpty() {
        activeTodos.shouldHave(size(0));
    }

    public void toggle(String text) {
        todos.findBy(exactText(text)).find(".toggle").click();
    }

    public void clearCompleted() {
        $("#clear-completed").click();
    }

    public void delete(String text) {
        todos.findBy(exactText(text)).hover()
                .find(".destroy").click();
    }

    public void footerShouldBeHidden() {
        $("#footer").shouldBe(hidden);
    }

    public void filterActive() {
        $("[href*='#/active']").click();
    }

    public void filterCompleted() {
        $("[href*='#/completed']").click();
    }

    public void filterAll() {
        $("[href*='#/']").click();
    }

    public EditableLabelItem editableLabel(String text){
        return new EditableLabelItem(todos.findBy(exactText(text)));
    }
}
