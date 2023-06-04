package com.findyourstampsvalue.aqa.pages;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.page;

public class ListOfLinksPage extends BasePage{

    ElementsCollection linksList=$$x("//a[@class='thumbnail']");

    @Step("Кликнуть по ссылке: '{0}'")
    public MainPage nextLinkClick(String linkName) {



        return page(MainPage.class);
    }

    
}
