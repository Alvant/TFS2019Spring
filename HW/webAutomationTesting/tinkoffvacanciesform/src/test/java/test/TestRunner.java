package test;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.concurrent.TimeUnit;


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

    @After
    public void tearDown() {
        this.app.driver.quit();
    }

    private void clickOnFormHeader() {
        this.app.driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
    }
}
