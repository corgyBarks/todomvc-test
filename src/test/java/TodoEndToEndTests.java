import com.codeborne.selenide.*;
import com.codeborne.selenide.impl.JavaScript;
import com.codeborne.selenide.impl.WebElementSource;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;

public class TodoEndToEndTests {
    private static final String NEW_TODO_INPUT = "#new-todo";
    private static final String CLEAR_COMPLETED = "#clear-completed";
    private static final String EDIT = ".edit";
    private static final String TOGGLE = ".toggle";
    private static final String DESTROY = ".destroy";
    private static final String EDITING = "editing";
    @Test
    public void todosLifeCycle() {
        Configuration.fastSetValue = true;
        openTodoPage();

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
    private static final ElementsCollection todos = $$("#todo-list li");
    private void openTodoPage() {
        open("http://todomvc4tasj.herokuapp.com/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return Object.keys(require.s.contexts._.defined).length === 39;"));
    }

    private void add(String... texts) {
        for(String text: texts) {
            $(NEW_TODO_INPUT).setValue(text).pressEnter();
        }
    }

    private void todosShouldBe(String... todoTexts) {
        todos.shouldHave(exactTexts(todoTexts));
    }

    private void edit(String text, String editedText) {
        startEdit(text, editedText).pressEnter();
    }

    private void toggle(String text) {
        todo(text).find(TOGGLE).click();

    }

    private void clearCompleted() {
        $(CLEAR_COMPLETED).click();
    }

    private void cancelEdit(String text, String editedText) {
        startEdit(text, editedText).pressEscape();
    }

    private void delete(String text) {
        todo(text).hover()
                .find(DESTROY).click();
    }
    private SelenideElement startEdit(String text, String newText) {
        doubleClickOnTask(text);
        return changeTextInTaskTo(newText);
    }
    private SelenideElement changeTextInTaskTo(String text) {
        return todos.findBy(cssClass(EDITING))
                .find(EDIT)
                .execute(new SetValueByJs(text));
    }
    private SelenideElement todo(String text) {
        return todos.findBy(exactText(text));
    }
    private void doubleClickOnTask(String text) {
        todo(text).doubleClick();
    }

    public static class SetValueByJs implements Command<SelenideElement> {
        private final JavaScript js = new JavaScript("set-value-without-blur.js");
        private final String text;

        public SetValueByJs(String text) {
            this.text = text;
        }
        @Override
        @Nonnull
        public SelenideElement execute(SelenideElement proxy, WebElementSource locator, Object[] args) {
            Driver driver = locator.driver();
            WebElement element = locator.findAndAssertElementIsEditable();
            this.js.execute(driver, element, this.text);
            return proxy;
        }
    }
}
