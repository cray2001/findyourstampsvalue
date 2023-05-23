package com.findyourstampsvalue.aqa;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.testng.ScreenShooter;
import com.findyourstampsvalue.aqa.pages.MainPage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import com.findyourstampsvalue.aqa.util.AllureScreenShooter;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;

//@Listeners(AllureScreenShooter.class)
public class BaseTest {
    public Logger log= LoggerFactory.getLogger(this.getClass());

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(){

        System.setProperty("chromeoptions.args", "--headless");

        log.info("Тест стартовал");

        ScreenShooter.captureSuccessfulTests = true;
        Configuration.timeout = 10000L;
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1840x1080";
        Configuration.browserPosition = "0x0";
        Configuration.fastSetValue = true;
        Configuration.pageLoadTimeout = 60000L;
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(){

        log.info("Тест завершён");
    }

    @Step("Открыть в браузере главную страницу сайта")
    public MainPage openMainPage() {



        open("https://findyourstampsvalue.com/");

        return page(MainPage.class);
    }

}
