package com.findyourstampsvalue.aqa;

import com.codeborne.selenide.*;
import com.findyourstampsvalue.aqa.pages.ListOfLinksPage;
import com.findyourstampsvalue.aqa.pages.MainPage;
import com.findyourstampsvalue.aqa.util.TestListener;
import com.findyourstampsvalue.aqa.util.HideMe;
import com.findyourstampsvalue.aqa.util.HideMeItem;
import com.google.gson.Gson;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.openqa.selenium.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;

@Listeners(TestListener.class)
public class BaseTest {
    public Logger log = LoggerFactory.getLogger(this.getClass());
    int i = 0;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {

        System.setProperty("chromeoptions.args", "--headless");

        log.info("Тесты стартовали");

        Configuration.pageLoadStrategy = "eager";
        //Configuration.holdBrowserOpen = true;
        Configuration.timeout = 30000L;
        //Configuration.browserSize = "1840x1080";
        //Configuration.browserPosition = "0x0";
        Configuration.fastSetValue = true;
        Configuration.pageLoadTimeout = 30000L;
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {

        File source = new File("config.properties");
        File dest = new File("./build/allure-results/environment.properties");

        try {
            Files.copy(source.toPath(), dest.toPath());
        } catch (IOException e) {
            log.info("Не удалось скопировать файл 'config.properties' в Allure-отчёт");
            e.printStackTrace();
        }

        log.info("Тесты завершены");
    }


    /**
     * Получает список актуальных HTTPS-прокси от HideMe
     * Выбирает 10 (или сколько есть) прокси US из разных городов
     * Выбирает 10 (или сколько есть) из других стран
     */
    @Step("Сформировать тестовый данные")
    public Object[][] getTestData() {

        Response response = given()
                .contentType("application/json")
                .when()
                .get("http://justapi.info/api/proxylist.php?out=js&maxtime=1000&type=s");

        //log.info("Ответ от justapi: {}",response.asPrettyString());

        String jsonString = response.asPrettyString().replaceAll("<.+?>", "");
        jsonString = String.join("", "{\"hideme\":", jsonString, "}");

        //log.info("После RegEx-a: {}",jsonString);

        Gson gson = new Gson();
        HideMe proxyList = gson.fromJson(jsonString, HideMe.class);

        log.info("Получено: {} HTTPS прокси", proxyList.getHideMeItemList().size());

        //Поделить список на два: US и другие.
        //Из списка US выбрать 10 самых быстрых из разных городов, если нет 10 то сколько есть.
        //Из списка других стран выбрать 10 самых быстрых из разных стран, если нет 10 то сколько есть.

        List<HideMeItem> usProxy = proxyList
                .getHideMeItemList()
                .stream()
                .filter(item -> item.getCountryCode().equals("US"))
                .sorted()
                .peek(this::addCityNameIfEmpty)
                .collect(Collectors.toList());

        usProxy = usProxy
                .stream()
                .sorted()
                .distinct()
                .limit(10)
                .collect(Collectors.toList());

        for (HideMeItem item : usProxy) {
            System.out.println(item.toString());
        }

        List<HideMeItem> otherProxy = proxyList
                .getHideMeItemList()
                .stream()
                .filter(item -> !item.getCountryCode().equals("US"))
                .sorted()
                .distinct()
                .limit(10)
                .collect(Collectors.toList());

        for (HideMeItem item : otherProxy) {
            System.out.println(item.toString());
        }

        Object[][] proxyArray = new Object[20][3];

        List<HideMeItem> commonList;
        commonList = Stream.of(otherProxy, usProxy)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Properties properties = new Properties();

        try (InputStream in = new FileInputStream("config.properties")) {
            properties.load(in);
        } catch (IOException e) {
            log.info("Не удалось прочитать файл: 'config.properties'");
            e.printStackTrace();
        }

        int lastTestRun = Integer.parseInt(properties.getProperty("LAST_TEST_RUN"));

        log.info("LAST_TEST_RUN={}", lastTestRun);

        int p = 0;
        for (HideMeItem item : commonList) {
            proxyArray[p][0] = item.getIp() + ":" + item.getPort();
            proxyArray[p][1] = item.getCountryName() + " (" + item.getCity() + ")";
            proxyArray[p][2] = Integer.parseInt(properties.getProperty("LAST_TEST_RUN")) + p;
            p++;
        }

        lastTestRun = lastTestRun + commonList.size();

        savePropertiesToFile(usProxy, otherProxy, lastTestRun);

        return proxyArray;
    }

    @Step("Сохранить настройки в файл")
    private void savePropertiesToFile(List<HideMeItem> usProxy, List<HideMeItem> otherProxy, int lastTestRun) {

        try (FileWriter fw = new FileWriter("config.properties")) {
            String line;
            int j = 0;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            fw.write("#Time of last test run: " + formatter.format(LocalDateTime.now()) + "\n");
            fw.write("#\n");
            fw.write("LAST_TEST_RUN=" + lastTestRun + "\n");
            fw.write("#\n");

            for (HideMeItem item : usProxy) {
                line = String.join("",
                        "US_PROXY_" + j++, "=",
                        item.getIp(), ":",
                        item.getPort(), " ",
                        item.getCountryName(), " (",
                        item.getCity(), ")\n");
                fw.write(line);
            }

            fw.write("#\n");

            j = 0;
            for (HideMeItem item : otherProxy) {
                line = String.join("",
                        "OTHER_PROXY_" + j++, "=",
                        item.getIp(), ":",
                        item.getPort(), " ",
                        item.getCountryName(), " (",
                        item.getCity(), ")\n");
                fw.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCityNameIfEmpty(HideMeItem item) {
        if (item.getCountryCode().equals("US") && item.getCity().equals("")) {
            item.setCity("ZZZ_" + i++);
        }
    }

    @Step("Задать прокси: '{0}', страна: '{1}'")
    public void setProxy(String proxyString, String country) {

        Proxy proxy = new Proxy();
        proxy.setSslProxy(proxyString);
        WebDriverRunner.setProxy(proxy);
        


        log.info("Прокси: '{}', страна: '{}'", proxyString, country);
        Allure.addAttachment("Прокси: '" + proxyString + "', страна: '" + country + "'", "");
    }

    @Step("Открыть страницу 'findyourstampsvalue.com/stamp/catcode/list'")
    public ListOfLinksPage openLinksPage() {

        open("https://findyourstampsvalue.com/stamp/catcode/list");

        //Проверка изменения IP в каждом тесте
        //open("https://pr-cy.ru/browser-details/");
        //$x("//div[@class='ip-myip']").shouldBe(Condition.visible);

        return page(ListOfLinksPage.class);
    }

    @Step("Открыть страницу 'findyourstampsvalue.com/stamp/catcode/list'")
    public MainPage openMainPage() {

        open("https://findyourstampsvalue.com");

        //Проверка изменения IP в каждом тесте
        //open("https://pr-cy.ru/browser-details/");
        //$x("//div[@class='ip-myip']").shouldBe(Condition.visible);

        return page(MainPage.class);
    }

}
