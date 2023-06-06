package com.findyourstampsvalue.aqa.util;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.reporters.ExitCodeListener;

public class TestListener extends ExitCodeListener {
    public static Logger log = LoggerFactory.getLogger(TestListener.class);

    public void onTestFailure(final ITestResult result) {
        //super.onTestFailure(result);
        //takeScreenshot();
        Selenide.closeWebDriver();
        log.info("Тест провален. Драйвер закрыт.");
    }

    public void onTestSuccess(final ITestResult result) {
        //super.onTestSuccess(result);
        //takeScreenshot();
        Selenide.closeWebDriver();
        log.info("Тест прошёл. Драйвер закрыт.");
    }

    @Attachment(value = "Screenshot", type = "image/png", fileExtension = ".png")
    public static byte[] takeScreenshot() {

        return Selenide.screenshot(OutputType.BYTES);
    }
}
