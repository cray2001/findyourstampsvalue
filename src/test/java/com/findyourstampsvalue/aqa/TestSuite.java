package com.findyourstampsvalue.aqa;

import org.testng.annotations.*;

public class TestSuite extends BaseTest {

    @DataProvider(name = "testData")
    public Object[][] dataProviderMethod() {
        return getTestData();
    }

    @Test(description = "Тест доступности 'findyourstampsvalue.com'", groups = {"ALL"}, dataProvider = "testData")
    void checkWebsiteAvailability(String proxyString, String country, int testRun) {

        setProxy(proxyString, country);

        openLinksPage()
                .checkNextLink(testRun)
                //.checkSearchByImage()
                .checkRegistration(testRun)
        ;
    }

    @Test(description = "Тесты без прокси ", groups = {"ALL"}, enabled = false)
    void checkWithoutProxy() {

        int testRun = 1000;

//        openLinksPage()
//                .checkNextLink(testRun)
//                .checkSearchByImage()
//                .checkRegistration(testRun)

        openMainPage()
                .checkSearchByImage()
        ;
    }

}
