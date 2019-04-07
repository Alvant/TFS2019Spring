package app;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.BrowsersFactory;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;


public class Application {
    Logger logger = LoggerFactory.getLogger(Application.class);

    public final String browserName = System.getProperty("browser") == null ? "chrome" : System.getProperty("browser");
    public WebDriver driver;
    public Ui ui;
    private WebDriverWait wait;

    public Application() throws MalformedURLException {
        driver = new EventFiringWebDriver(getDriver());
        ((EventFiringWebDriver) driver).register(new BrowsersFactory.MyListener());
        wait = new WebDriverWait(driver, 10);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        ui = new Ui(driver);
    }

    public void quit() {
        driver.quit();
        driver = null;
    }

    private WebDriver getDriver() throws MalformedURLException {
        return BrowsersFactory.buildDriver(browserName);
    }
}
