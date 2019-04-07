package extensions.Ui;

import org.openqa.selenium.By;

public class TextInput extends BaseElement {
    public TextInput(String name) {
        super(name);
    }

    public By get() {
        return By.xpath(String.format(
                "//span[contains(text(), \"%s\")]/preceding::input[1]",
                this.name
        ));
    }
}
