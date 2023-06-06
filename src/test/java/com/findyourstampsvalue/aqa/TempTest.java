package com.findyourstampsvalue.aqa;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.openqa.selenium.Proxy;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

/**
 * Нужен только для отладки
 */
public class TempTest extends BaseTest {

    @Test(groups = {"ALL"})
    void tempTest() {

        Proxy proxy = new Proxy();
        proxy.setSslProxy("41.76.145.18:8080");
        WebDriverRunner.setProxy(proxy);

        open("https://pr-cy.ru/browser-details/");
    }

}
