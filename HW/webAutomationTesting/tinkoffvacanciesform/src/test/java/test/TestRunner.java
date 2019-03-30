package test;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;
import java.util.concurrent.TimeUnit;


public class TestRunner {
    private WebDriver driver;
    private String baseUrl;

    @Before
    public void setUp() {
        System.setProperty(
                "webdriver.gecko.driver",
                "/home/alvant/browser-drivers/geckodriver");

//        System.setProperty(
//                "webdriver.chrome.driver",
//                "/home/alvant/browser-drivers/chromedriver");
//
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments(
////                "--headless",
////                "window-size=1024,768",
//                "--no-sandbox");
//        options.setBinary("/home/alvant/browser-executables/chromium-browser");
//
//        driver = new ChromeDriver(options);

//        System.setProperty(
//                "webdriver.firefox.bin",
//                "/home/alvant/browser-executables/firefox");

        File pathBinary = new File("/home/alvant/browser-executables//firefox");
        FirefoxBinary firefoxBinary = new FirefoxBinary(pathBinary);
        FirefoxOptions options = new FirefoxOptions().setBinary(firefoxBinary);

        driver = new FirefoxDriver(options);

        baseUrl = "https://www.tinkoff.ru/career/vacancies/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void test1() {
        driver.get(baseUrl);

        driver.findElement(By.name("name")).click();
        driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();

        driver.findElement(By.name("birthday")).click();
        driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
        assertEquals("Поле обязательное", driver.findElement(By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-inputdate\")][.//span[text()=\"Дата рождения\"]]/following-sibling::div[contains(@class,\"error-message\")]")).getText());

        driver.findElement(By.name("city")).click();
        driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();

        driver.findElement(By.name("email")).click();
        driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();

        driver.findElement(By.name("phone")).click();
        driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();

        driver.findElement(By.name("socialLink0")).click();
        driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
    }

    @Test
    public void test2() {
        driver.get("https://www.tinkoff.ru/career/vacancies/");

        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("1");
        driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
        assertEquals("Допустимо использовать только буквы русского алфавита и дефис", driver.findElement(By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-suggest\")][.//span[text()=\"Фамилия и имя\"]]/following-sibling::div[contains(@class,\"error-message\")]")).getText());

        driver.findElement(By.name("birthday")).click();
        driver.findElement(By.name("birthday")).clear();
        driver.findElement(By.name("birthday")).sendKeys("1");
        driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
        assertEquals("Поле заполнено некорректно", driver.findElement(By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-inputdate\")][.//span[text()=\"Дата рождения\"]]/following-sibling::div[contains(@class,\"error-message\")]")).getText());

        driver.findElement(By.name("city")).click();
        driver.findElement(By.name("city")).clear();
        driver.findElement(By.name("city")).sendKeys("1");
        driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();

        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("1");
        driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
        assertEquals("Введите корректный адрес эл. почты", driver.findElement(By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-input\")][.//span[text()=\"Электронная почта\"]]/following-sibling::div[contains(@class,\"error-message\")]")).getText());

        driver.findElement(By.name("phone")).click();
        driver.findElement(By.name("phone")).clear();
        driver.findElement(By.name("phone")).sendKeys("+7(1");
        driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
        assertEquals("Номер телефона должен состоять из 10 цифр, начиная с кода оператора", driver.findElement(By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-input\")][.//span[text()=\"Мобильный телефон\"]]/following-sibling::div[contains(@class,\"error-message\")]")).getText());

        driver.findElement(By.name("socialLink0")).click();
        driver.findElement(By.name("socialLink0")).clear();
        driver.findElement(By.name("socialLink0")).sendKeys("1");
        driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
