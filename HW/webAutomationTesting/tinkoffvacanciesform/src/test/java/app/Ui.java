package app;

import extensions.Ui.Checkbox;
import extensions.Ui.Select;
import extensions.Ui.SelectOption;
import extensions.Ui.TextInput;
import org.openqa.selenium.WebDriver;

public class Ui {
    private static WebDriver driver;

    public Ui(WebDriver driver) {
        Ui.driver = driver;
    }

    public static String getValue(TextInput input) {
        return driver.findElement(input.get()).getText();
    }

    public static void setValue(TextInput input, String value) {
        driver.findElement(input.get()).sendKeys(value);
    }

    public static void selectOption(Select s, SelectOption o) {
        driver.findElement(s.get()).click();
        driver.findElement(o.get()).click();
    }

    public static String getSelectedOption(Select s) {
        return driver.findElement(s.get()).getText();
    }

    public static void setChecked(Checkbox cb) {
        driver.findElement(cb.getCheckControl()).click();
    }

    public static boolean isChecked(Checkbox cb) {
        return driver.findElement(cb.get()).isSelected();
    }

    public static void setUnchecked(Checkbox cb) {
        if (Ui.isChecked(cb)) {
            Ui.setChecked(cb);
        }
    }

    public static String getCheckboxVisibleName(Checkbox cb) {
        return driver.findElement(cb.getNameControl()).getText();
    }
}
