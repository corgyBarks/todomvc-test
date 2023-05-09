package com.taotas.todomvctest;

import com.codeborne.selenide.Command;
import com.codeborne.selenide.SelenideElement;
import com.taotas.todomvctest.utils.commands.SetValueByJs;

public class Action{
    public static class JS {
        public static Command<SelenideElement> setValue(String text){
            return new SetValueByJs(text);

        }
    }

}