package com.taotas.todomvctest.pages;

import com.codeborne.selenide.*;
import com.taotas.todomvctest.BaseTest;
import com.taotas.todomvctest.utils.Action;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TodoMvcPage extends BaseTest {
    public static final ElementsCollection todos = $$("#todo-list li");

    public void toggleAll() {
        $("#toggle-all").click();
    }

    public void itemsLeftShouldBe(int counter) {
        $("#todo-count>strong").shouldHave(text(String.valueOf(counter)));
    }

    public void verifyTodosEmpty() {
        todos.shouldHave(size(0));
    }

    public void givenAppOpened() {
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


    public void add(String... texts) {
        for (String text : texts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    public void todosShouldBe(String... todoTexts) {
        todos.filter(visible).shouldHave(exactTexts(todoTexts));
    }

    public void completedTodosShouldBe(String... todoTexts) {
        todos.filterBy(Condition.cssClass("completed")).shouldHave(exactTexts(todoTexts));
    }

    public void activeTodosShouldBe(String... todoTexts) {
        todos.filterBy(Condition.cssClass("active")).shouldHave(exactTexts(todoTexts));
    }

    public void completedTodosShouldBeEmpty() {
        todos.filterBy(Condition.cssClass("completed")).shouldHave(size(0));
    }

    public void activeTodosShouldBeEmpty() {
        todos.filterBy(Condition.cssClass("active")).shouldHave(size(0));
    }

    public void edit(String text, String editedText) {
        startEdit(text, editedText).pressEnter();
    }

    public void editByTab(String text, String editedText) {
        startEdit(text, editedText).pressTab();
    }

    public void toggle(String text) {
        todos.findBy(exactText(text)).find(".toggle").click();

    }

    public void clearCompleted() {
        $("#clear-completed").click();
    }

    public void cancelEdit(String text, String editedText) {
        startEdit(text, editedText).pressEscape();
    }

    public void delete(String text) {
        todos.findBy(exactText(text)).hover()
                .find(".destroy").click();
    }

    public SelenideElement startEdit(String text, String newText) {
        todos.findBy(exactText(text)).doubleClick();
        return todos.findBy(cssClass("editing"))
                .find(".edit")
                .execute(Action.JS.setValue(newText));
    }

    public void verifyFooterIsHidden() {
        $("#footer").shouldBe(hidden);
    }

    public void verifyFooterIsVisible() {
        $("#footer").shouldBe(visible);
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
}
