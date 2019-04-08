package pages.elements;

import org.openqa.selenium.By;

public class TinkoffVacanciesPageElements {
    private String url = "https://www.tinkoff.ru/career/vacancies/";

    public String getUrl() {
        return this.url;
    }

    public By getNameControl() {
        return By.name("name");
    }

    public By getBirthdayControl() {
        return By.name("birthday");
    }

    public By getEmptyBirthdayErrorMessageControl() {
        return By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-inputdate\")][.//span[text()=\"Дата рождения\"]]/following-sibling::div[contains(@class,\"error-message\")]");
    }

    public By getInvalidNameErrorMessageControl() {
        return By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-suggest\")][.//span[text()=\"Фамилия и имя\"]]/following-sibling::div[contains(@class,\"error-message\")]");
    }

    public By getInvalidBirthdayErrorMessageControl() {
        return By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-inputdate\")][.//span[text()=\"Дата рождения\"]]/following-sibling::div[contains(@class,\"error-message\")]");
    }

    public By getInvalidEmailErrorMessageControl() {
        return By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-input\")][.//span[text()=\"Электронная почта\"]]/following-sibling::div[contains(@class,\"error-message\")]");
    }

    public By getInvalidPhoneErrorMessageControl() {
        return By.xpath("//div[@data-qa-file=\"CareerForm\"]//div[contains(@class, \"ui-input\")][.//span[text()=\"Мобильный телефон\"]]/following-sibling::div[contains(@class,\"error-message\")]");
    }

    public By getCityControl() {
        return By.name("city");
    }

    public By getEmailControl() {
        return By.name("email");
    }

    public By getPhoneControl() {
        return By.name("phone");
    }

    public By getSocialLinkControl() {
        return By.name("socialLink0");
    }

    public By getFormHeaderControl() {
        return By.xpath("//div[text()=\"Заполните анкету\"]");
    }
}
