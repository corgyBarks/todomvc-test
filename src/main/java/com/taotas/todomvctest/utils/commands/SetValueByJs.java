package com.taotas.todomvctest.utils.commands;

import com.codeborne.selenide.Command;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.impl.JavaScript;
import com.codeborne.selenide.impl.WebElementSource;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;

public class SetValueByJs implements Command<SelenideElement> {
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
