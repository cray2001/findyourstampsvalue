package com.findyourstampsvalue.aqa;

import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import org.testng.annotations.Test;

@Feature("FindYourStampValue")
public class CheckTextSearch extends BaseTest{

    @Test(description = "Проверка работы текстового поиска ", groups = {"ALL"})
    
    void checkTextSearch(){

        openMainPage()
                .inputStampDescription()
                .checkStampsAreFound()
                ;

    }

}
