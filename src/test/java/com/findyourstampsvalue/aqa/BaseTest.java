package com.findyourstampsvalue.aqa;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.findyourstampsvalue.aqa.pages.ListOfLinksPage;
import com.findyourstampsvalue.aqa.pages.MainPage;
import com.findyourstampsvalue.aqa.util.HideMe;
import com.findyourstampsvalue.aqa.util.HideMeItem;
import com.findyourstampsvalue.aqa.util.TestListener;
import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.openqa.selenium.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static io.restassured.RestAssured.given;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Listeners(TestListener.class)
public class BaseTest {
    public Logger log = LoggerFactory.getLogger(this.getClass());
    int i = 0;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {

        //System.setProperty("chromeoptions.args", "--headless");
        log.info("Тесты стартовали");

        Configuration.savePageSource = false;
        Configuration.screenshots = false;
        Configuration.headless = true;
        //Configuration.pageLoadStrategy = "eager";
        //Configuration.holdBrowserOpen = true;
        Configuration.timeout = 60000L;
        //Configuration.browserSize = "1840x1080";
        //Configuration.browserPosition = "0x0";
        Configuration.fastSetValue = true;
        Configuration.pageLoadTimeout = 60000L;
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
File source = new File("config.properties");
        File dest = new File("./build/allure-results/environment.properties");

        try {
            Files.copy(source.toPath(), dest.toPath(), REPLACE_EXISTING);
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
    @Step("Сформировать тестовые данные")
    public Object[][] getTestData() {

        Response response = given()
                .contentType("application/json")
                .when()
                .get("http://justapi.info/api/proxylist.php?out=js&maxtime=1000&type=s&code=958218285534530");

        //log.info("Ответ от justapi: {}",response.asPrettyString());

        String jsonString = response.asPrettyString().replaceAll("<.+?>", "");
        jsonString = String.join("", "{\"hideme\":", jsonString, "}");

        //log.info("JSON после обработки: {}",jsonString);

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

        List<HideMeItem> commonList;
        commonList = Stream.of(otherProxy, usProxy)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Object[][] proxyArray = new Object[commonList.size()][3];

        Properties properties = new Properties();

        try (InputStream in = new FileInputStream("config.properties")) {
            properties.load(in);


        } catch (IOException e) {
            log.info("Не удалось прочитать файл: 'config.properties'");
            e.printStackTrace();
        }

        File propertiesFile= new File("config.properties");
        log.info("Файл: 'config.properties' удалён - {}",propertiesFile.delete());

        int lastTestRun = Integer.parseInt(properties.getProperty("LAST_TEST_RUN"));

        log.info("Прочитал свойство: LAST_TEST_RUN={}", properties.getProperty("LAST_TEST_RUN"));

        int p = 0;
        for (HideMeItem item : commonList) {
            proxyArray[p][0] = item.getIp() + ":" + item.getPort();
            proxyArray[p][1] = item.getCountryName() + " (" + item.getCity() + ")";
            proxyArray[p][2] = lastTestRun + p;
            p++;
        }

        log.info("lastTestRun={}",lastTestRun);
        log.info("commonList.size()={}",commonList.size());

        lastTestRun = lastTestRun + commonList.size();

        log.info("lastTestRun={}",lastTestRun);

        savePropertiesToFile(usProxy, otherProxy, lastTestRun);

        return proxyArray;
    }

    @Step("Сохранить настройки в файл")
    private void savePropertiesToFile(List<HideMeItem> usProxy, List<HideMeItem> otherProxy, int lastTestRun) {

        try (FileWriter fw = new FileWriter("config.properties", false)) {
            String line;
            int j = 0;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            fw.write("#Time of last test run: " + formatter.format(LocalDateTime.now()) + "\n");
            fw.write("#\n");
            fw.write("LAST_TEST_RUN=" + lastTestRun + "\n");
            fw.write("#\n");

            log.info("Записал свойство: LAST_TEST_RUN={}", lastTestRun);

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

        log.info("Параметры последнего запуска сохранены в 'config.properties'");
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
        //Allure.addAttachment("Прокси: '" + proxyString + "', страна: '" + country + "'", "");
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
