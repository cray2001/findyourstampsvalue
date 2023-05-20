package com.findyourstampsvalue.aqa;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

/**
 * Используется для отладки методов вместо main
 */


public class TempTest {

    @Test(groups = {"ALL"})
    void tempTest(){
        //ChromeOptions options = new ChromeOptions();
        //options.addArguments("--remote-allow-origins=*");
        //WebDriver driver = new ChromeDriver(options);
        WebDriver driver = new ChromeDriver();

        //driver.get("ya.ru");
        driver.get("https://www.selenium.dev/selenium/web/web-form.html");
    }


}
