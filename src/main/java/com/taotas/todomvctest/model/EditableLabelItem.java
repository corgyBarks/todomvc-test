package com.taotas.todomvctest.model;

import com.codeborne.selenide.SelenideElement;
import com.taotas.todomvctest.Action;

import static com.codeborne.selenide.Selenide.$;

public class EditableLabelItem {
    private final SelenideElement element;


    public EditableLabelItem(SelenideElement element) {
        this.element = element;
    }

    public void edit(String newText){
        startEdit(newText).pressEnter();
    }

    public void cancelEdit(String editedText) {
        startEdit(editedText).pressEscape();
    }

    public void editByTab(String editedText) {
        startEdit(editedText).pressTab();
    }

    public SelenideElement startEdit(String newText) {
        element.doubleClick();
        return $(".editing")
                .find(".edit")
                .execute(Action.JS.setValue(newText));
    }

}
