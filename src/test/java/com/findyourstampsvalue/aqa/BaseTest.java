package com.findyourstampsvalue.aqa;

import com.codeborne.selenide.Configuration;
import com.findyourstampsvalue.aqa.pages.ListOfLinksPage;
import com.findyourstampsvalue.aqa.util.AllureScreenShooter;
import com.findyourstampsvalue.aqa.util.HideMe;
import com.findyourstampsvalue.aqa.util.HideMeItem;
import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

@Listeners(AllureScreenShooter.class)
public class BaseTest {
    public Logger log = LoggerFactory.getLogger(this.getClass());
    int i = 0;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {

        //System.setProperty("chromeoptions.args", "--headless");

        log.info("Тест стартовал");

        Configuration.holdBrowserOpen = true;
        Configuration.timeout = 60000L;
        Configuration.browserSize = "1840x1080";
        Configuration.browserPosition = "0x0";
        Configuration.fastSetValue = true;
        Configuration.pageLoadTimeout = 60000L;

        //fillProxyList();

        /*Properties properties=new Properties();
        //properties.

        try {

            properties.load(new FileInputStream("config.properties"));

//            String host = properties.getProperty("db.host");
//            appProps.setProperty("name", "NewAppName"); // update an old value


        } catch (IOException e) {
            log.info("ОШИБКА: Не удалось найти файл 'config.properties'");
        }*/
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {

        log.info("Тест завершён");
    }

    /**
     * Получает список актуальных HTTPS-прокси от HideMe
     * Выбирает 10 (или сколько есть) прокси US из разных городов
     * Выбирает 10 (или сколько есть) из других стран
     */
    @Step("Заполнить список HTTPS-прокси")
    public Object[][] fillProxyList() {


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
            log.info(item.toString());
        }

        Properties properties = new Properties();
        Properties temp = new Properties();

        try (InputStream in = new FileInputStream("config.properties")) {
            properties.load(in);

        } catch (IOException e) {
            log.info("Не удалось прочитать файл: 'config.properties'");
            e.printStackTrace();
        }

        try (FileWriter fw = new FileWriter("config.properties")) {
            String line;
            int j = 0;
            for (HideMeItem item : usProxy) {
                //properties.setProperty("US_PROXY_" + j++, item.getIp() + ":" + item.getPort() + " " + item.getCountryName() + " " + item.getCity());

                line = "US_PROXY_" + j++ + "=" + item.getIp() + ":" + item.getPort() + " " + item.getCountryName() + " " + item.getCity()+"\n";
                //fw.append(line);
                fw.write(line);
            }

            j = 0;
            for (HideMeItem item : otherProxy) {
                //properties.setProperty("OTHER_PROXY_" + j++, item.getIp() + ":" + item.getPort() + " " + item.getCountryName() + " " + item.getCity());

                line = "OTHER_PROXY_" + j++ + "=" + item.getIp() + ":" + item.getPort() + " " + item.getCountryName() + " " + item.getCity()+"\n";
                //fw.append(line);
                fw.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try (OutputStream out = new FileOutputStream("config.properties")) {
//            properties.store(out, "Параметры последнего запуска:");
//
//        } catch (IOException e) {
//            log.info("Не удалось записать данные в файл: 'config.properties'");
//            e.printStackTrace();
//        }

        return new Object[][]{};
    }

    private void addCityNameIfEmpty(HideMeItem item) {
        if (item.getCountryCode().equals("US") && item.getCity().equals("")) {
            item.setCity("ZZZ_" + i++);
        }
    }

    @Step("Открыть страницу 'findyourstampsvalue.com/stamp/catcode/list'")
    public ListOfLinksPage openLinksPage() {

        open("https://findyourstampsvalue.com/stamp/catcode/list");

        return page(ListOfLinksPage.class);
    }

}
