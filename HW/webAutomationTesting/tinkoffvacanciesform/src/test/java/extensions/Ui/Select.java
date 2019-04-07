package extensions.Ui;

import org.openqa.selenium.By;

public class Select extends BaseElement {
    public Select(String name) {
        super(name);
    }

    public By get() {
        return By.xpath(String.format(
                "//span[contains(@class, \"select\") and text()=\"%s\"]",
                this.name));
    }
}
