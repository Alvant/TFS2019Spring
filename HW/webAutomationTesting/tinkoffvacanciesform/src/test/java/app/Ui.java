package app;

import extensions.Ui.Checkbox;
import extensions.Ui.Select;
import extensions.Ui.SelectOption;
import extensions.Ui.TextInput;
import org.openqa.selenium.WebDriver;

public class Ui {
    private WebDriver driver;

    public Ui(WebDriver driver) {
        this.driver = driver;
    }

    public String getValue(TextInput input) {
        return this.driver.findElement(input.get()).getText();
    }

    public void setValue(TextInput input, String value) {
        this.driver.findElement(input.get()).sendKeys(value);
    }

    public void selectOption(Select s, SelectOption o) {
        this.driver.findElement(s.get()).click();
        this.driver.findElement(o.get()).click();
    }

    public String getSelectedOption(Select s) {
        return this.driver.findElement(s.get()).getText();
    }

    public void setChecked(Checkbox cb) {
        this.driver.findElement(cb.get()).click();
    }

    public boolean isChecked(Checkbox cb) {
        return this.driver.findElement(cb.get()).isSelected();
    }

    public String getCheckboxVisibleName(Checkbox cb) {
        return this.driver.findElement(cb.getName()).getText();
    }
}
