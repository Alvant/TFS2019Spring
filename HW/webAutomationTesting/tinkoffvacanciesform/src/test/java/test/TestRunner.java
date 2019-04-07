package test;


import app.Ui;
import extensions.Ui.Checkbox;
import extensions.Ui.Select;
import extensions.Ui.SelectOption;
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

        this.app.driver.findElement(By.name("q")).clear();
        this.app.driver.findElement(By.name("q")).sendKeys("мобайл тинькофф");
        this.app.driver.findElement(By.xpath("//span[text()=\"мобайл тинькофф\"][./b[text()=\" тарифы\"]]")).click();
        this.app.driver.findElement(By.cssSelector("a[href=\"https://www.tinkoff.ru/mobile-operator/tariffs/\"]")).click();

        String currentHandle = this.getNthTabAndSwitchTo(1);
        this.confirmRegionIfConfirmationExists();

        assertEquals(
                "Тарифы Тинькофф Мобайла",
                this.app.driver.findElement(By.cssSelector("[name=\"titleAndSubtitleBlock\"] h2")).getText());

        this.closeAllTabsExceptAndSwitchTo(currentHandle);

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

        this.app.driver.get("https://www.tinkoff.ru/mobile-operator/tariffs/");
        this.confirmRegionIfConfirmationExists();

        this.selectRegion(regionTitlesShort.get("moskva"));
        assertEquals(
                regionTitlesFull.get("moskva"),
                this.app.driver.findElement(By.xpath("//div[contains(@class, \"MvnoRegionConfirmation__title\")]")).getText()
        );

        // refresh and check if region stays
        this.app.driver.navigate().refresh();
        assertEquals(
                regionTitlesFull.get("moskva"),
                this.app.driver.findElement(By.xpath("//div[contains(@class, \"MvnoRegionConfirmation__title\")]")).getText()
        );

        for (String region : regionTitlesFull.keySet()) {
            this.selectRegion(regionTitlesShort.get(region));
            regionPricesDefault.put(
                    region,
                    Integer.parseInt(String.join("", this.getTotalPriceMatch().group(2).split("\\s")))
            );

            Ui.selectOption(
                    new Select("Интернет"),
                    new SelectOption("Безлимитный интернет")
            );

            Ui.selectOption(
                    new Select("Звонки"),
                    new SelectOption("Безлимитные минуты")
            );

            regionPricesFull.put(
                    region,
                    Integer.parseInt(String.join("", this.getTotalPriceMatch().group(2).split("\\s")))
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

        this.app.driver.get("https://www.tinkoff.ru/mobile-operator/tariffs/");

        Ui.selectOption(
                new Select("Интернет"),
                new SelectOption("0 ГБ")
        );

        Ui.selectOption(
                new Select("Звонки"),
                new SelectOption("0 минут")
        );

        this.uncheckAllCheckboxes();

        finalPrice = this.getTotalPriceMatch().group(1);

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

    private String getNthTabAndSwitchTo(int nth) {
        ArrayList tabs = new ArrayList (this.app.driver.getWindowHandles());
        String targetHandle = (String) tabs.get(nth);
        this.app.driver.switchTo().window(targetHandle);

        return targetHandle;
    }

    private void closeAllTabsExceptAndSwitchTo(String targetHandle) {
        for(String handle : this.app.driver.getWindowHandles()) {
            if (!handle.equals(targetHandle)) {
                this.app.driver.switchTo().window(handle);
                this.app.driver.close();
            }
        }

        this.app.driver.switchTo().window(targetHandle);
    }

    private boolean isElementExists(By control) {
        boolean isExists = false;

        try {
            this.app.driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
            isExists = this.app.driver.findElements(control).size() != 0;
        } catch (Error e) {
            e.printStackTrace();
        } finally {
            this.app.driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        }

        return isExists;
    }

    private Matcher getTotalPriceMatch() {
        final Pattern p = Pattern.compile("Общая цена: (([\\d|\\s]+) \u20BD)");
        Matcher m;

        m = p.matcher(this.app.driver.findElement(By.xpath("//h3[contains(text(), \"Общая цена\")]")).getText());

        if (m.find() != true) {
            throw new IllegalStateException("Price not found on page");
        }

        return m;
    }

    private void confirmRegionIfConfirmationExists() {
        By regionConfirmation = By.xpath("//span[text()=\"Да\" and contains(@class, \"MvnoRegionConfirmation\")]");

        if (this.isElementExists(regionConfirmation)) {
            this.app.driver.findElement(regionConfirmation).click();
        }
    }

    private void uncheckAllCheckboxes() {
        String checkboxesNames[] = {
                "Мессенджеры",
                "Социальные сети",
                "Безлимитные СМС",
                "Музыка",
                "Видео"
        };

        for (String name : checkboxesNames) {
            Ui.setUnchecked(new Checkbox(name));
        }
    }

    private void selectRegion(String regionName) {
        WebDriverWait wait = new WebDriverWait(this.app.driver, 10);

        this.app.driver.findElement(By.xpath("//div[contains(@class, \"MvnoRegionConfirmation__title\")]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[text()=\"Выберите регион\"]")));
        this.app.driver.findElement(By.xpath(String.format("//div[text()=\"%s\"]", regionName))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(), \"Общая цена\")]")));
    }
}
