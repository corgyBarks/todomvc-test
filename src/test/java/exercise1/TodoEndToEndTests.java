package exercise1;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Selenide.*;

public class TodoEndToEndTests {

    @Test
    public void basicEndToEndTestCase() {
//        Configuration.timeout = 6000;
        open("http://todomvc4tasj.herokuapp.com/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return Object.keys(require.s.contexts._.defined).length === 39;"));

        $("#new-todo").setValue("a").pressEnter();
        $("#new-todo").setValue("b").pressEnter();
        $("#new-todo").setValue("c").pressEnter();
        $("#new-todo").setValue("d").pressEnter();
        $$("#todo-list li").shouldHave(exactTexts("a", "b", "c", "d"));
        $$("#todo-list>li").shouldHave(size(4));

        $("#todo-list li:nth-of-type(2) .toggle").click();
        $$("#todo-list li.completed")
                .shouldHave(exactTexts("b"));
        $$("#todo-list>li:not(.completed)")
                .shouldHave(exactTexts("a", "c", "d"));


        $("#todo-list li:nth-of-type(4)").hover();
        $("#todo-list li:nth-of-type(4) .destroy").click();
        $$("#todo-list>li").shouldHave(exactTexts("a", "b", "c"));

        $("#filters > li:nth-child(2)").click();
        $$("#todo-list li.active").shouldHave(exactTexts("a", "c"));

        $("#filters > li:nth-child(3)").click();
        $$("#todo-list li.completed").shouldHave(exactTexts("b"));

        $("#clear-completed").click();
        $$("#todo-list li.completed").shouldBe(empty);
    }

}
