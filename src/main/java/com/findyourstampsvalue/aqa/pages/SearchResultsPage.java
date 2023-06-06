package com.findyourstampsvalue.aqa.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

public class SearchResultsPage extends BasePage<SearchResultsPage> {

    SelenideElement registrationLink = $x("//a[@class='join']");
    SelenideElement buyFreeButton = $x("//span[@data-product-name='Worries free']/following-sibling::button");

    SelenideElement signupHeader = $x("//div[contains(text(),'Create account')]");

    SelenideElement emailInput = $x("//input[@id='registerFormEmail']");
    SelenideElement passInput = $x("//input[@id='registerFormPassword']");
    SelenideElement passRepeatButton = $x("//input[@id='registerFormPasswordRepeat']");

    SelenideElement signupButton = $x("//button[contains(text(),'Sign up')]");

    SelenideElement searchHeader = $x("//span[contains(text(),'Worry-free Plan')]");


    @Step("Проверить что найдена хотя бы одна марка")
    public void checkRegistration(int testRun) {

        String email=  "test"+testRun+"@fysv.com";

        registrationLink.shouldBe(Condition.visible).click();

        log.info("Кликнул по кнопке 'Join'");
        Allure.addAttachment("Кликнул по кнопке 'Join'", "");

        buyFreeButton.shouldBe(Condition.exist).click();

        log.info("Кликнул по кнопке 'Worries free'");
        Allure.addAttachment("Кликнул по кнопке 'Worries free'", "");

        signupHeader.shouldBe(Condition.appear);

        log.info("Заголовок 'Create account' появился");
        Allure.addAttachment("Заголовок 'Create account' появился", "");

        emailInput.shouldBe(Condition.visible).sendKeys(email);
        passInput.shouldBe(Condition.visible).sendKeys("QdenU@5Dzx");
        passRepeatButton.shouldBe(Condition.visible).sendKeys("QdenU@5Dzx");

        signupButton.shouldBe(Condition.visible).click();

        log.info("Кликнул по кнопке 'Sign up'");
        Allure.addAttachment("Кликнул по кнопке 'Sign up'", "");

        searchHeader.shouldBe(Condition.appear);

        log.info("Регистрация с email: '{}' завершена успешно",email);
        Allure.addAttachment("Регистрация с email: '"+email+"' завершена успешно", "");
    }

}
