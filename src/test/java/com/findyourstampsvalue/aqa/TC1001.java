package com.findyourstampsvalue.aqa;

import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import org.testng.annotations.Test;

@Feature("FindYourStampValue")
public class TC1001 extends BaseTest{

    @Test(description = "Проверка работы текстового поиска ", groups = {"ALL"})
    @Link(name="checkTextSearch",url="")
    void checkTextSearch(){

        openMainPage()
                .inputStampDescription()
                .checkStampsAreFound()
                ;

    }

}
