package com.findyourstampsvalue.aqa;

import com.codeborne.selenide.WebDriverRunner;
import com.findyourstampsvalue.aqa.util.AllureScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import org.openqa.selenium.Proxy;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;

//@Feature("FindYourStampValue")
public class TestSuite extends BaseTest {


    @DataProvider(name = "proxyList")
    public Object[][] dataProviderMethod() {
        return fillProxyList();
    }

    @Test(description = "Проверка работы текстового поиска ", groups = {"ALL"}, dataProvider = "proxyList")
    void checkTextSearch(String proxyString, String country) {

        log.info("Прокси: '{}', страна: '{}'", proxyString, country);
        addStep(proxyString, country);

        Proxy proxy = new Proxy();
        proxy.setSslProxy(proxyString);
        WebDriverRunner.setProxy(proxy);
        //open("https://www.showmyip.com/");

        //open("http://findyourstampsvalue.com/");

//        sleep(10*1000);
//        AllureScreenShooter.takeScreenshot();
//        closeWebDriver();
 
        openMainPage()
                .inputStampDescription()
                .checkStampsAreFound()
                ;

        AllureScreenShooter.takeScreenshot();
        closeWebDriver();
    }

    @Step("Прокси: '{0}', страна: '{1}'")
    void addStep(String proxyString, String country) {
    }

}