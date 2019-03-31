package test;


import com.google.common.io.Files;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


public class BrowsersFactory {
    private static String webDriversPath = System.getProperty("webdriverspath");

    public static class MyListener extends AbstractWebDriverEventListener {

        Logger logger = LoggerFactory.getLogger(BrowsersFactory.class);

        @Override
        public void beforeFindBy(By by, WebElement element, WebDriver driver) {
            logger.info("Обращение к элементу " + by);
        }

        @Override
        public void afterFindBy(By by, WebElement element, WebDriver driver) {
            logger.info("Найден элемент " + by);
        }

        @Override
        public void onException(Throwable throwable, WebDriver driver) {
            File tmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File file = new File("target", "sccreen-" + System.currentTimeMillis() + ".png");
            try {
                Files.copy(tmp, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.error(file.getAbsolutePath());
        }
    }

    public static WebDriver buildDriver(String browserName) throws MalformedURLException {
        if (browserName.equals("chrome")) {
            if (webDriversPath != null) {
                System.setProperty(
                    "webdriver.chrome.driver",
                    String.format("%s\\chromedriver.exe", System.getProperty("webdriverspath")));
            }

            ChromeOptions chromeOpt = new ChromeOptions();
            chromeOpt.addArguments("--disable-notifications");

            return new ChromeDriver(chromeOpt);
        } else if (browserName.equals("firefox")) {
            if (webDriversPath != null) {
                System.setProperty(
                    "webdriver.gecko.driver",
                    String.format("%s\\geckodriver.exe", System.getProperty("webdriverspath")));
            }

            //Disable login to console and redirect log to an external file
            System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");

            FirefoxOptions ffOpt = new FirefoxOptions();
            ffOpt.addPreference("dom.webnotifications.enabled", false);

            return new FirefoxDriver(ffOpt);
        } else if (browserName.equals("opera")) {
            if (webDriversPath != null) {
                System.setProperty(
                    "webdriver.opera.driver",
                    String.format("%s\\operadriver.exe", System.getProperty("webdriverspath")));
            }

            return new OperaDriver();
        } else {
            throw new IllegalArgumentException(String.format(
                    "Unknown browser \"%s\"", browserName
            ));
        }
    }
}