package extensions.Ui;

import org.openqa.selenium.By;

public class Checkbox extends BaseElement {
    public Checkbox(String name) {
        super(name);
    }

    public By get() {
        return By.xpath(String.format(
                "//label[contains(text(), \"%s\")]/preceding::input[1]",
                this.name)
        );
    }

    public By getName() {
        return By.xpath(String.format("//label[contains(text(), \"%s\")]"));
    }
}
