package com.example.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TC11 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://www.katalon.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testTC11() throws Exception {
    driver.get("https://www.tinkoff.ru/career/vacancies/");
    driver.findElement(By.name("name")).click();
    driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
    driver.findElement(By.name("birthday")).click();
    driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
    driver.findElement(By.name("city")).click();
    driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
    driver.findElement(By.name("email")).click();
    driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
    driver.findElement(By.name("phone")).click();
    driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
    driver.findElement(By.name("socialLink0")).click();
    driver.findElement(By.xpath("//div[text()=\"Заполните анкету\"]")).click();
    assertEquals("Поле обязательное", driver.findElement(By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[@class=\"ui-inputdate\"]/following-sibling::div[contains(@class,\"error-message\")]")).getText());
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
