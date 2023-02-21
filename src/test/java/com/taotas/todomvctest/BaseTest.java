package com.taotas.todomvctest;

import com.codeborne.selenide.Configuration;

public class BaseTest {
    {
        Configuration.fastSetValue = true;
        Configuration.baseUrl = System.getProperty("baseUrl", "http://todomvc4tasj.herokuapp.com");
    }
}
