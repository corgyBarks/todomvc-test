import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;

public class TodoEndToEndTests {
    private static final ElementsCollection todoList = $$("#todo-list li");

    @Test
    public void todosLifeCycle() {
        open("http://todomvc4tasj.herokuapp.com/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return Object.keys(require.s.contexts._.defined).length === 39;"));

        add("a", "b", "c");
        verifyTodoListHaveTexts("a", "b", "c");

        doubleClickOnTaskWithText("b");
        changeTextInTaskTo(" edited").pressEnter();

        completeTaskWithText("b edited");
        clearAllCompletedTasks();
        verifyTodoListHaveTexts("a", "c");

        doubleClickOnTaskWithText("c");
        changeTextInTaskTo(" to be canceled").pressEscape();

        deleteTaskWithText("c");
        verifyTodoListHaveTexts("a");

    }

    private void verifyTodoListHaveTexts(String... texts){
        todoList.shouldHave(exactTexts(texts));
    }
    private void deleteTaskWithText(String text){
        todoList.findBy(exactText(text)).hover()
                .find(".destroy").click();
    }
    private void clearAllCompletedTasks(){
        $("#clear-completed").click();
    }

    private void completeTaskWithText(String text){
        todoList.findBy(exactText(text))
                .find(" .toggle").click();
    }
    private SelenideElement changeTextInTaskTo(String text) {
        return todoList.findBy(cssClass("editing"))
                .find(" .edit")
                .append(text);
    }

    private void doubleClickOnTaskWithText(String text) {
        $$("#todo-list li").findBy(exactText(text)).doubleClick();
    }

    private void add(String... taskText) {
        for (String text : taskText) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

}
