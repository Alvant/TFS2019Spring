package extensions.Ui;

import org.openqa.selenium.By;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BaseElement {
    protected String name;

    public BaseElement(String name) {
        this.name = name;
    }

    public By get() {
        throw new NotImplementedException();
    }
}
