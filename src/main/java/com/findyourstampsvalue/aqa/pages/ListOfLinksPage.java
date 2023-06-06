package com.findyourstampsvalue.aqa.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.*;

public class ListOfLinksPage extends BasePage<ListOfLinksPage> {

    ElementsCollection linkList = $$x("//a[@class='thumbnail']");
    SelenideElement findResult=$x("//div[@class='search-notifications-header']");

    @Step("Кликнуть по ссылке: '{0}'")
    public SearchResultsPage checkNextLink(int linkIndex) {

        int linkCount = linkList.size();
        int index = linkIndex <= linkCount ? linkIndex : linkIndex % linkCount;

        SelenideElement link= linkList.get(index);

        log.info("Кликнул по ссылке: '{}'", link.text());
        Allure.addAttachment("Кликнул по ссылке: '"+ link.text()+"'", "");

        link.shouldBe(Condition.exist).click();

        findResult.shouldBe(Condition.exist);

        int result=Integer.parseInt(findResult.text().split(" ")[1]);

        Assert.assertTrue(result!=0,"Найдено НОЛЬ результатов");

        log.info("Найдено марок: '{}'", result);
        Allure.addAttachment("Найдено марок: '"+result+"'", "");

        return page(SearchResultsPage.class);
    }


}
