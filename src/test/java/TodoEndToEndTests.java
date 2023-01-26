import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TodoEndToEndTests {

    @Test
    public void todosLifeCycle() {
        openTodoPage();
        addTodo("a");
        addTodo("b");
        addTodo("c");
        verifyTodosDisplayed("a", "b", "c");
        editTodoWithText("b", " edited");
        completeTodo("b edited");
        clearCompletedTodos();
        verifyTodosDisplayed("a", "c");
        cancelEditTodo("c", " to be canceled");
        deleteTodo("c");
        verifyTodosDisplayed("a");
    }


    private void openTodoPage() {
        open("http://todomvc4tasj.herokuapp.com/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return Object.keys(require.s.contexts._.defined).length === 39;"));
    }

    private void addTodo(String text) {
        $("#new-todo").setValue(text).pressEnter();
    }

    private void verifyTodosDisplayed(String... todoTexts) {
        $$("#todo-list li").shouldHave(exactTexts(todoTexts));
    }

    private void editTodoWithText(String originalText, String editedText) {
        $$("#todo-list li").findBy(exactText(originalText)).doubleClick();
        $$("#todo-list li").findBy(cssClass("editing"))
                .find(" .edit")
                .append(editedText).pressEnter();
    }

    private void completeTodo(String text) {
        $$("#todo-list li").findBy(exactText(text))
                .find(" .toggle").click();

    }

    private void clearCompletedTodos() {
        $("#clear-completed").click();
    }

    private void cancelEditTodo(String originalText, String editedText) {
        $$("#todo-list li").findBy(exactText(originalText)).doubleClick();
        $$("#todo-list li").findBy(cssClass("editing"))
                .find(" .edit")
                .append(editedText).pressEscape();
    }

    private void deleteTodo(String text) {
        $$("#todo-list li").findBy(exactText(text)).hover()
                .find(".destroy").click();
    }
}
