package com.findyourstampsvalue.aqa.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import java.io.File;

import static com.codeborne.selenide.Selenide.*;

public class MainPage extends BasePage<MainPage> {

    SelenideElement mainSearch = $x("//input[@id='mainSearch']");
    SelenideElement submitButton = $x("//button[@type='submit' and @class='crop-submit-button']");

    @Step("Ввести текст в строку поиска и нажать Enter")
    public SearchResultsPage checkSearchByImage() {

        SelenideElement findResult = $x("//div[@class='search-notifications-header']");
        SelenideElement imageInput = $x("//input[@id='imageSearchInput']");

        imageInput.uploadFile(new File("src/test/resources/jenny-test.jpg"));
        log.info("Марка с самолётом загружена.");

        submitButton.shouldBe(Condition.visible).click();

        log.info("Кнопка 'Submit' нажата.");

        int result = Integer.parseInt(findResult.text().split(" ")[1]);

        Assert.assertTrue(result != 0, "Найдено НОЛЬ результатов");

        log.info("Найдено совпадений: '{}'", result);
        Allure.addAttachment("Найдено совпадений: '" + result + "'", "");

        return page(SearchResultsPage.class);
    }
}
