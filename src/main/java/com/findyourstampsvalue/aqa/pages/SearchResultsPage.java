package com.findyourstampsvalue.aqa.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.*;

public class SearchResultsPage extends BasePage<SearchResultsPage>{

ElementsCollection foundStamps=$$x("//img[@class='lazy entered loaded']");



SelenideElement notification=$x("//div[@class='search-notifications-header']");

@Step("Проверить что найдена хотя бы одна марка")
public void checkStampsAreFound(){

    Assert.assertTrue(notification.isDisplayed(), "На странице отсутствует 'search-notifications-header'");
    Assert.assertFalse(foundStamps.isEmpty(),"Не найдено ни одной марки");

    log.info("Открыта страница результатов поиска");
    Allure.addAttachment("Открыта страница результатов поиска","");
}

}
