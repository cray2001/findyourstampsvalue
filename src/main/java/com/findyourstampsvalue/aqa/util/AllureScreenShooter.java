package com.findyourstampsvalue.aqa.util;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.reporters.ExitCodeListener;

public class AllureScreenShooter extends ExitCodeListener {
    public static Logger log = LoggerFactory.getLogger(AllureScreenShooter.class);

    public void onTestFailure(final ITestResult result) {

        log.info("AllureScreenShooter.onTestFailure");

        super.onTestFailure(result);
        takeScreenshot();
    }

    public void onTestSuccess(final ITestResult result) {

        log.info("AllureScreenShooter.onTestSuccess");

        super.onTestSuccess(result);
        takeScreenshot();
    }

    @Attachment(value = "Screenshot", type = "image/png", fileExtension = ".png")
    public static byte[] takeScreenshot() {

        return Selenide.screenshot(OutputType.BYTES);
    }
}
