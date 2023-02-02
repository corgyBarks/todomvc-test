import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TodoEndToEndTests {

    @Test
    public void todosLifeCycle() {
        Configuration.fastSetValue = true;
        openTodoPage();

        add("a", "b", "c");
        verifyTodosDisplayed("a", "b", "c");

        edit("b", "b edited");

        toggle("b edited");
        clearCompleted();
        verifyTodosDisplayed("a", "c");

        cancelEdit("c", " to be canceled");

        delete("c");
        verifyTodosDisplayed("a");
    }

    private void openTodoPage() {
        open("http://todomvc4tasj.herokuapp.com/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return Object.keys(require.s.contexts._.defined).length === 39;"));
    }

    private void add(String... texts) {
        for(String text: texts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    private void verifyTodosDisplayed(String... todoTexts) {
        $$("#todo-list li").shouldHave(exactTexts(todoTexts));
    }

    private void edit(String originalText, String editedText) {
        $$("#todo-list>li").findBy(exactText(originalText)).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing"))
                .find(" .edit")
                .setValue(editedText).pressEnter();
    }

    private void toggle(String text) {
        $$("#todo-list li").findBy(exactText(text))
                .find(" .toggle").click();

    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void cancelEdit(String originalText, String editedText) {
        $$("#todo-list>li").findBy(exactText(originalText)).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing"))
                .find(" .edit")
                .append(editedText).pressEscape();
    }

    private void delete(String text) {
        $$("#todo-list li").findBy(exactText(text)).hover()
                .find(".destroy").click();
    }
}
