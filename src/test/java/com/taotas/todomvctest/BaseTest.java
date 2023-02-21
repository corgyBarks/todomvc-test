package com.taotas.todomvctest;

import com.codeborne.selenide.Configuration;

import static java.lang.System.getProperty;

public class BaseTest {
    {
        Configuration.fastSetValue = true;
        Configuration.baseUrl =  getProperty(
                "selenide.baseUrl", "http://todomvc4tasj.herokuapp.com");
    }
}
