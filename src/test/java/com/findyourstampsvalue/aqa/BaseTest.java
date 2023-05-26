package com.findyourstampsvalue.aqa;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.testng.ScreenShooter;
import com.findyourstampsvalue.aqa.pages.MainPage;
import com.findyourstampsvalue.aqa.util.AllureScreenShooter;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;

//@Listeners(AllureScreenShooter.class)
public class BaseTest {
    public Logger log = LoggerFactory.getLogger(this.getClass());

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {

        System.setProperty("chromeoptions.args", "--headless");

        log.info("Тест стартовал");

//        Configuration.holdBrowserOpen = true;
        Configuration.timeout = 60000L;
        Configuration.browserSize = "1840x1080";
        Configuration.browserPosition = "0x0";
        Configuration.fastSetValue = true;
        Configuration.pageLoadTimeout = 60000L;
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {

        log.info("Тест завершён");
    }

    /**
     * Получает десять первых HTTPS-прокси со страницы "https://hidemy.name/ru/proxy-list"
     * Не работает из-за Cloudflare
     */
    @Step("Заполнить список HTTPS-прокси")
    public Object[][] fillProxyList() {

/*        open("https://hidemy.name/ru/proxy-list");
        $x("//input[@id='t_s']/parent::label").shouldBe(Condition.exist).click();
        $x("//button[text()='Показать']").shouldBe(Condition.visible).click();
        ElementsCollection records = $$x("//tbody/tr");
        String ip, port, country, speed;

        Object[][] proxy = new String[1][10];

        for (int i = 0; i < proxy[0].length; i++) {

            ip = records.get(i).$x("./td[1]").text();
            port = records.get(i).$x("./td[2]").text();
            country = records.get(i).$x("./td[3]").text();
            //speed = records.get(i).$x("./td[4]").text();
            proxy[0][i] = new Object[]{String.join(":", ip, port), country};
        }*/

        //return proxy;

        return new Object[][]{
                {"5.78.81.37:8080", "United States Portland"},
                {"167.86.99.172:8080", "Germany Nuremberg"},
                {"135.181.193.60:8080", "Finland Helsinki"},
                {"5.78.66.168:8080", "United States Portland"},
                {"138.201.174.153:8080", "Germany"},
                {"162.212.155.187:8080", "United States"},
                {"107.152.42.222:8080", "United States Chicago"},
                {"162.212.154.177:8080", "United States Chicago"},
                {"162.212.157.23:8080", "United States"},
                {"162.212.156.132:8080", "United States"},
        };
    }

    @Step("Открыть в браузере главную страницу сайта")
    public MainPage openMainPage() {

        open("https://findyourstampsvalue.com/");

        return page(MainPage.class);
    }

}
