package test;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestRunner extends BaseRunner {
    private String baseUrl = "https://www.tinkoff.ru/career/vacancies/";

    @Test
    public void test1() {
        this.app.driver.get(baseUrl);

        this.app.driver.findElement(By.name("name")).click();
        this.clickOnFormHeader();

        this.app.driver.findElement(By.name("birthday")).click();
        this.clickOnFormHeader();
        assertEquals(
                "Поле обязательное",
                this.app.driver.findElement(By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-inputdate\")][.//span[text()=\"Дата рождения\"]]/following-sibling::div[contains(@class,\"error-message\")]")).getText());

        this.app.driver.findElement(By.name("city")).click();
        this.clickOnFormHeader();

        this.app.driver.findElement(By.name("email")).click();
        this.clickOnFormHeader();

        this.app.driver.findElement(By.name("phone")).click();
        this.clickOnFormHeader();

        this.app.driver.findElement(By.name("socialLink0")).click();
        this.clickOnFormHeader();
    }

    @Test
    public void test2() {
        this.app.driver.get(baseUrl);

        this.app.driver.findElement(By.name("name")).click();
        this.app.driver.findElement(By.name("name")).clear();
        this.app.driver.findElement(By.name("name")).sendKeys("1");
        this.clickOnFormHeader();
        assertEquals(
                "Допустимо использовать только буквы русского алфавита и дефис",
                this.app.driver.findElement(By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-suggest\")][.//span[text()=\"Фамилия и имя\"]]/following-sibling::div[contains(@class,\"error-message\")]")).getText());

        this.app.driver.findElement(By.name("birthday")).click();
        this.app.driver.findElement(By.name("birthday")).clear();
        this.app.driver.findElement(By.name("birthday")).sendKeys("1");
        this.clickOnFormHeader();
        assertEquals(
                "Поле заполнено некорректно",
                this.app.driver.findElement(By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-inputdate\")][.//span[text()=\"Дата рождения\"]]/following-sibling::div[contains(@class,\"error-message\")]")).getText());

        this.app.driver.findElement(By.name("city")).click();
        this.app.driver.findElement(By.name("city")).clear();
        this.app.driver.findElement(By.name("city")).sendKeys("1");
        this.clickOnFormHeader();

        this.app.driver.findElement(By.name("email")).click();
        this.app.driver.findElement(By.name("email")).clear();
        this.app.driver.findElement(By.name("email")).sendKeys("1");
        this.clickOnFormHeader();
        assertEquals(
                "Введите корректный адрес эл. почты",
                this.app.driver.findElement(By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-input\")][.//span[text()=\"Электронная почта\"]]/following-sibling::div[contains(@class,\"error-message\")]")).getText());

        this.app.driver.findElement(By.name("phone")).click();
        this.app.driver.findElement(By.name("phone")).clear();
        this.app.driver.findElement(By.name("phone")).sendKeys("+7(1");
        this.clickOnFormHeader();
        assertEquals(
                "Номер телефона должен состоять из 10 цифр, начиная с кода оператора",
                this.app.driver.findElement(By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-input\")][.//span[text()=\"Мобильный телефон\"]]/following-sibling::div[contains(@class,\"error-message\")]")).getText());

        this.app.driver.findElement(By.name("socialLink0")).click();
        this.app.driver.findElement(By.name("socialLink0")).clear();
        this.app.driver.findElement(By.name("socialLink0")).sendKeys("1");
        this.clickOnFormHeader();
    }

    @Test
    public void test3() {
        this.app.driver.get("https://www.google.ru/");

        this.app.driver.findElement(By.name("q"))
                .clear();
        this.app.driver.findElement(By.name("q"))
                .sendKeys("мобайл тинькофф");
        this.app.driver.findElement(By.xpath("//span[text()=\"мобайл тинькофф\"][./b[text()=\" тарифы\"]]"))
                .click();
        this.app.driver.findElement(By.cssSelector("a[href=\"https://www.tinkoff.ru/mobile-operator/tariffs/\"]"))
                .click();

        ArrayList tabs = new ArrayList (this.app.driver.getWindowHandles());
        System.out.println(tabs.size());
        String currentHandle = (String) tabs.get(1);
        this.app.driver.switchTo().window(currentHandle);

        this.app.driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        boolean exists = this.app.driver.findElements( By.xpath("//span[text()=\"Да\" and contains(@class, \"MvnoRegionConfirmation\")]") ).size() != 0;
        this.app.driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        if (exists) {
            this.app.driver.findElement(By.xpath("//span[text()=\"Да\" and contains(@class, \"MvnoRegionConfirmation\")]")).click();
        }
        assertEquals(
                "Тарифы Тинькофф Мобайла",
                this.app.driver.findElement(By.cssSelector("[name=\"titleAndSubtitleBlock\"] h2")).getText());

        for(String handle : this.app.driver.getWindowHandles()) {
            if (!handle.equals(currentHandle)) {
                this.app.driver.switchTo().window(handle);
                this.app.driver.close();
            }
        }
        this.app.driver.switchTo().window(currentHandle);

        assertEquals(
                "https://www.tinkoff.ru/mobile-operator/tariffs/",
                this.app.driver.getCurrentUrl());
    }

    @Test
    public void test4() {
        final Map<String, String> regionTitlesFull = new HashMap<String, String>();
        final Map<String, String> regionTitlesShort = new HashMap<String, String>();
        final Map<String, Integer> regionPricesDefault = new HashMap<String, Integer>();
        final Map<String, Integer> regionPricesFull = new HashMap<String, Integer>();

        regionTitlesFull.put("moskva", "Москва и Московская область");
        regionTitlesFull.put("krasnodar", "Краснодарский край");

        regionTitlesShort.put("moskva", "Москва и Московская обл.");
        regionTitlesShort.put("krasnodar", "Краснодарский кр.");

        WebDriverWait wait = new WebDriverWait(this.app.driver, 10);

        final Pattern p = Pattern.compile("Общая цена: ([\\d|\\s]+) \u20BD");
        Matcher m;

        this.app.driver.get("https://www.tinkoff.ru/mobile-operator/tariffs/");

        // ***
        //this.app.driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        boolean exists = this.app.driver.findElements( By.xpath("//span[text()=\"Да\" and contains(@class, \"MvnoRegionConfirmation\")]") ).size() != 0;
        //this.app.driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        if (exists) {
            this.app.driver.findElement(By.xpath("//span[text()=\"Да\" and contains(@class, \"MvnoRegionConfirmation\")]")).click();
        }
        // ***

        this.app.driver.findElement(By.xpath("//div[contains(@class, \"MvnoRegionConfirmation__title\")]"))
                .click();
        System.out.println(String.format("//div[text()=\"%s\"]", regionTitlesShort.get("moskva")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[text()=\"Выберите регион\"]")));
        this.app.driver.findElement(By.xpath(String.format("//div[text()=\"%s\"]", regionTitlesShort.get("moskva"))))
                .click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(), \"Общая цена\")]")));
        assertEquals(
                regionTitlesFull.get("moskva"),
                this.app.driver.findElement(By.xpath("//div[contains(@class, \"MvnoRegionConfirmation__title\")]"))
                        .getText()
        );

        this.app.driver.navigate().refresh();
        assertEquals(
                regionTitlesFull.get("moskva"),
                this.app.driver.findElement(By.xpath("//div[contains(@class, \"MvnoRegionConfirmation__title\")]"))
                        .getText()
        );

        for (String region : regionTitlesFull.keySet()) {
            this.app.driver.findElement(By.xpath("//div[contains(@class, \"MvnoRegionConfirmation__title\")]"))
                    .click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[text()=\"Выберите регион\"]")));
            this.app.driver.findElement(By.xpath(String.format("//div[text()=\"%s\"]", regionTitlesShort.get(region))))
                    .click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(), \"Общая цена\")]")));
            m = p.matcher(this.app.driver.findElement(By.xpath("//h3[contains(text(), \"Общая цена\")]")).getText());
            assertEquals(true, m.find());
            regionPricesDefault.put(
                    region,
                    Integer.parseInt(String.join("", m.group(1).split("\\s")))
            );

            this.app.driver.findElement(By.xpath("//span[contains(@class, \"select\") and text()=\"Интернет\"]"))
                    .click();
            this.app.driver.findElement(By.xpath("//div[./span[contains(@class, \"dropdown\") and text()=\"Безлимитный интернет\"]]"))
                    .click();

            this.app.driver.findElement(By.xpath("//span[contains(@class, \"select\") and text()=\"Звонки\"]"))
                    .click();
            this.app.driver.findElement(By.xpath("//div[./span[contains(@class, \"dropdown\") and text()=\"Безлимитные минуты\"]]"))
                    .click();

            m = p.matcher(this.app.driver.findElement(By.xpath("//h3[contains(text(), \"Общая цена\")]")).getText());
            assertEquals(true, m.find());
            regionPricesFull.put(
                    region,
                    Integer.parseInt(String.join("", m.group(1).split("\\s")))
            );
        }

        assertNotEquals(
                regionPricesDefault.get("moskva"),
                regionPricesDefault.get("krasnodar")
        );

        assertEquals(
                regionPricesFull.get("moskva"),
                regionPricesFull.get("krasnodar")
        );
    }

    @Test
    public void test5() {
        String finalPrice;
        final Pattern p = Pattern.compile("Общая цена: ([\\d|\\s]+ \u20BD)");
        Matcher m;

        this.app.driver.get("https://www.tinkoff.ru/mobile-operator/tariffs/");

        this.app.driver.findElement(By.xpath("//span[contains(@class, \"select\") and text()=\"Интернет\"]"))
                .click();
        this.app.driver.findElement(By.xpath("//div[./span[contains(@class, \"dropdown\") and text()=\"0 ГБ\"]]"))
                .click();

        this.app.driver.findElement(By.xpath("//span[contains(@class, \"select\") and text()=\"Звонки\"]"))
                .click();
        this.app.driver.findElement(By.xpath("//div[./span[contains(@class, \"dropdown\") and text()=\"0 минут\"]]"))
                .click();

        if ( this.app.driver.findElement(By.xpath("//input[@id=\"2050\"]")).isSelected() ) {
            this.app.driver.findElement(By.xpath("//div[./input[@id=\"2050\"]]")).click();
        }
        if ( this.app.driver.findElement(By.xpath("//input[@id=\"2053\"]")).isSelected() ) {
            this.app.driver.findElement(By.xpath("//div[./input[@id=\"2053\"]]")).click();
        }
        if ( this.app.driver.findElement(By.xpath("//input[@id=\"2046\"]")).isSelected() ) {
            this.app.driver.findElement(By.xpath("//div[./input[@id=\"2046\"]]")).click();
        }
        if ( this.app.driver.findElement(By.xpath("//input[@id=\"2047\"]")).isSelected() ) {
            this.app.driver.findElement(By.xpath("//div[./input[@id=\"2047\"]]")).click();
        }
        if ( this.app.driver.findElement(By.xpath("//input[@id=\"2048\"]")).isSelected() ) {
            this.app.driver.findElement(By.xpath("//div[./input[@id=\"2048\"]]")).click();
        }

        m = p.matcher(this.app.driver.findElement(By.xpath("//h3[contains(text(), \"Общая цена\")]")).getText());
        assertEquals(true, m.find());
        finalPrice = m.group(1);

        assertEquals("0 \u20BD", finalPrice);
        assertEquals(true, this.app.driver.findElement(By.xpath("//div[text()=\"Заказать сим-карту\"]")).isEnabled());
    }

    @After
    public void tearDown() {
        this.app.driver.quit();
    }

    private void clickOnFormHeader() {
        this.app.driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
    }
}
