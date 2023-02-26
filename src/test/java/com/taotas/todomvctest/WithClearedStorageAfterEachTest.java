package com.taotas.todomvctest;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class WithClearedStorageAfterEachTest extends BaseTest {
    @BeforeEach
    public void loadApp() {
        open();
        boolean hasLocalStorageData = executeJavaScript("return Object.keys(localStorage).length > 0");
        if (hasLocalStorageData) {
            clearData();
        }
    }

    public void clearData() {
        Selenide.clearBrowserLocalStorage();
    }

    private void open() {
        Selenide.open("/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return Object.keys(require.s.contexts._.defined).length === 39;"));
    }
}
