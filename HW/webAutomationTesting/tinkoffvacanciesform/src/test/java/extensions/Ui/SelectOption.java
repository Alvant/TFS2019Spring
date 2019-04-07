package extensions.Ui;

import org.openqa.selenium.By;

public class SelectOption extends BaseElement {
    public SelectOption(String name) {
        super(name);
    }

    public By get() {
        return By.xpath(String.format(
                "//div[./span[contains(@class, \"dropdown\") and text()=\"%s\"]]",
                this.name));
    }
}
