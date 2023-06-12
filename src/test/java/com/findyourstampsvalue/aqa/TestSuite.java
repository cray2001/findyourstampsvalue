package com.findyourstampsvalue.aqa;

import com.findyourstampsvalue.aqa.pages.SearchResultsPage;
import org.testng.annotations.*;

public class TestSuite extends BaseTest {

    @DataProvider(name = "testData")
    public Object[][] dataProviderMethod() {
        return getTestData();
    }

    @Test(description = "HTTPS-прокси: ", groups = {"ALL"}, dataProvider = "testData")
    void checkWebsiteAvailability(String proxyString, String country, int testRun) {

        setProxy(proxyString, country);

        SearchResultsPage resultPage = openLinksPage()
                .checkNextLink(testRun);
        //.checkSearchByImage()

        if (testRun % 100 == 0) {
            resultPage.checkRegistration(testRun);
        }
    }
}
