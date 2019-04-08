package app;

import extensions.Ui.Checkbox;
import extensions.Ui.Select;
import extensions.Ui.SelectOption;
import extensions.Ui.TextInput;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ui {
    private static Logger logger = LoggerFactory.getLogger(Ui.class);
    private static WebDriver driver;

    public Ui(WebDriver driver) {
        Ui.driver = driver;
    }

    public static void open(String url) {
        logger.info("start open(" + url + ")");
        driver.get(url);
        logger.info("finish open(" + url + ")");
    }

    public static void click(By element) {
        logger.info("start click(" + element + ")");
        driver.findElement(element).click();
        logger.info("finish click(" + element + ")");
    }

    public static void clear(By element) {
        logger.info("start clear(" + element + ")");
        driver.findElement(element).clear();
        logger.info("finish clear(" + element + ")");
    }

    public static String getText(By element) {
        logger.info("start getText(" + element + ")");
        String text = driver.findElement(element).getText();
        logger.info("finish getText(" + element + ")");

        return text;
    }

    public static String getValue(TextInput input) {
        logger.info("start getValue(" + input + ")");
        String text = driver.findElement(input.get()).getText();
        logger.info("finish getValue(" + input + ")");

        return text;
    }

    public static void setValue(TextInput input, String value) {
        logger.info("start setValue(" + input + ", " + value + ")");
        driver.findElement(input.get()).sendKeys(value);
        logger.info("finish setValue(" + input + ", " + value + ")");
    }

    public static void setValueBy(By element, String value) {
        logger.info("start setValueBy(" + element + ", " + value + ")");
        driver.findElement(element).sendKeys(value);
        logger.info("finish setValueBy(" + element + ", " + value + ")");
    }

    public static void selectOption(Select s, SelectOption o) {
        logger.info("start selectOption(" + s + ", " + o + ")");
        driver.findElement(s.get()).click();
        driver.findElement(o.get()).click();
        logger.info("finish selectOption(" + s + ", " + o + ")");
    }

    public static String getSelectedOption(Select s) {
        logger.info("start getSelectedOption(" + s + ")");
        String text = driver.findElement(s.get()).getText();
        logger.info("finish getSelectedOption(" + s + ")");

        return text;
    }

    public static void setChecked(Checkbox cb) {
        logger.info("start setChecked(" + cb + ")");
        driver.findElement(cb.getCheckControl()).click();
        logger.info("finish setChecked(" + cb + ")");
    }

    public static boolean isChecked(Checkbox cb) {
        logger.info("start isChecked(" + cb + ")");
        boolean result = driver.findElement(cb.get()).isSelected();
        logger.info("finish isChecked(" + cb + ")");

        return result;
    }

    public static void setUnchecked(Checkbox cb) {
        logger.info("start setUnchecked(" + cb + ")");
        if (Ui.isChecked(cb)) {
            Ui.setChecked(cb);
        }
        logger.info("finish setUnchecked(" + cb + ")");
    }

    public static String getCheckboxVisibleName(Checkbox cb) {
        logger.info("start getCheckboxVisibleName(" + cb + ")");
        String text = driver.findElement(cb.getNameControl()).getText();
        logger.info("finish getCheckboxVisibleName(" + cb + ")");

        return text;
    }
}
