package com.findyourstampsvalue.aqa.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.page;

public class MainPage extends BasePage<MainPage> {

    SelenideElement mainSearch = $x("//input[@id='mainSearch']");

    @Step("Ввести текст в строку поиска и нажать Enter")
    public SearchResultsPage inputStampDescription() {

        //Faker faker = new Faker();
        //String text = faker.harryPotter().character();
        String text = "stamp";

        mainSearch.shouldBe(Condition.visible).sendKeys(text);
        mainSearch.sendKeys(Keys.ENTER);

        log.info("В поле поиска введена строка: '{}'",text);
        Allure.addAttachment("В поле поиска введена строка: '"+text+"'","");

        return page(SearchResultsPage.class);
    }
}
