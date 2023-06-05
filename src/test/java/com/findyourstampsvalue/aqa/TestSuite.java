package com.findyourstampsvalue.aqa;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.Proxy;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestSuite extends BaseTest {

    @DataProvider(name = "testData")
    public Object[][] dataProviderMethod() {
        return getTestData();
    }

    @Test(description = "Тест доступности 'findyourstampsvalue.com'", groups = {"ALL"}, dataProvider = "testData")
    void checkWebsiteAvailability(String proxyString, String country, int testRun) {

        setProxy(proxyString,country);

        openLinksPage()
//                .checkNextLink(testRun)
//                .inputStampDescription()
//                .checkStampsAreFound()
        ;

        //WebDriverRunner.getWebDriver().close();
    }


}
