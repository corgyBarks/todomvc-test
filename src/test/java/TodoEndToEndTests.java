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

    private void todosShouldBe(String... todoTexts) {
        $$("#todo-list li").shouldHave(exactTexts(todoTexts));
    }

    private void edit(String text, String editedText) {
        $$("#todo-list>li").findBy(exactText(text)).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing"))
                .find(" .edit")
                .execute(new SetValueByJs(editedText)).pressEnter();
    }

    private void toggle(String text) {
        $$("#todo-list li").findBy(exactText(text))
                .find(" .toggle").click();

    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void cancelEdit(String text, String editedText) {
        $$("#todo-list>li").findBy(exactText(text)).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing"))
                .find(" .edit")
                .execute(new SetValueByJs(editedText)).pressEscape();
    }

    private void delete(String text) {
        $$("#todo-list li").findBy(exactText(text)).hover()
                .find(".destroy").click();
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