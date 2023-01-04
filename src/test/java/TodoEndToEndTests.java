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
    private static final ElementsCollection todos = $$("#todo-list li");

    @Test
    public void todosLifeCycle() {
        open("http://todomvc4tasj.herokuapp.com/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return Object.keys(require.s.contexts._.defined).length === 39;"));

        add("a", "b", "c");
        assertTodos("a", "b", "c");

        startEdit("b", " edited").pressEnter();

        todo("b edited").find(" .toggle").click();
        $("#clear-completed").click();
        assertTodos("a", "c");

        startEdit("c", " to be canceled").pressEscape();

        todo("c").hover().find(".destroy").click();
        assertTodos("a");

    }

    private SelenideElement startEdit(String oldText, String textToAdd) {
        doubleClickOnTask(oldText);
        return changeTextInTaskTo(textToAdd);
    }

    private void assertTodos(String... texts) {
        todos.shouldHave(exactTexts(texts));
    }

    private SelenideElement todo(String text) {
        return todos.findBy(exactText(text));
    }

    private SelenideElement changeTextInTaskTo(String text) {
        return todos.findBy(cssClass("editing"))
                .find(" .edit")
                .append(text);
    }

    private void doubleClickOnTask(String text) {
        todo(text).doubleClick();
    }

    private void add(String... texts) {
        for (String text : texts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

}
