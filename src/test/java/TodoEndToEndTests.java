import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;

public class TodoEndToEndTests {

    @Test
    public void todosLifeCycle() {
        open("http://todomvc4tasj.herokuapp.com/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return Object.keys(require.s.contexts._.defined).length === 39;"));

        $("#new-todo").setValue("a").pressEnter();
        $("#new-todo").setValue("b").pressEnter();
        $("#new-todo").setValue("c").pressEnter();
        $$("#todo-list li").shouldHave(exactTexts("a", "b", "c"));

        $$("#todo-list>li").findBy(exactText("b")).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing"))
                .find(" .edit")
                .append(" edited").pressEnter();

        $$("#todo-list>li").findBy(exactText("b edited"))
                .find(" .toggle").click();
        $("#clear-completed").click();
        $$("#todo-list>li").shouldBe(exactTexts("a","c"));

        $$("#todo-list>li").findBy(exactText("c")).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing"))
                .find(" .edit")
                .append(" to be canceled").pressEscape();

        $$("#todo-list>li").findBy(exactText("c")).hover()
                .find(".destroy").click();
        $$("#todo-list>li").shouldHave(exactTexts("a"));


    }

}
