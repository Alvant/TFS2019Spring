package pages;

import app.Ui;
import extensions.Ui.TextInput;
import pages.elements.TinkoffVacanciesPageElements;

public class TinkoffVacanciesPage extends BasePage {
    private static TinkoffVacanciesPageElements elements = new TinkoffVacanciesPageElements();

    public static void open() {
        Ui.open(elements.getUrl());
    }

    public static void clickName() {
        Ui.click(elements.getNameControl());
    }

    public static void clickBirthday() {
        Ui.click(elements.getBirthdayControl());
    }

    public static String getEmptyBirthdayErrorMessageText() {
        return Ui.getText(elements.getEmptyBirthdayErrorMessageControl());
    }

    public static String getInvalidNameErrorMessageText() {
        return Ui.getText(elements.getInvalidNameErrorMessageControl());
    }

    public static String getInvalidBirthdayErrorMessageText() {
        return Ui.getText(elements.getInvalidBirthdayErrorMessageControl());
    }

    public static String getInvalidEmailErrorMessageText() {
        return Ui.getText(elements.getInvalidEmailErrorMessageControl());
    }

    public static String getInvalidPhoneErrorMessageText() {
        return Ui.getText(elements.getInvalidPhoneErrorMessageControl());
    }

    public static void clickCity() {
        Ui.click(elements.getCityControl());
    }

    public static void clickEmail() {
        Ui.click(elements.getEmailControl());
    }

    public static void clickPhone() {
        Ui.click(elements.getPhoneControl());
    }

    public static void clickSocialLink() {
        Ui.click(elements.getSocialLinkControl());
    }

    public static void clickOnFormHeader() {
        Ui.click(elements.getFormHeaderControl());
    }

    public static void clearName() {
        Ui.clear(elements.getNameControl());
    }

    public static void setName(String value) {
        Ui.setValueBy(elements.getNameControl(), value);
    }

    public static void clearBirthday() {
        Ui.clear(elements.getBirthdayControl());
    }

    public static void setBirthday(String value) {
        Ui.setValueBy(elements.getBirthdayControl(), value);
    }

    public static void clearCity() {
        Ui.clear(elements.getCityControl());
    }

    public static void setCity(String value) {
        Ui.setValueBy(elements.getCityControl(), value);
    }

    public static void clearEmail() {
        Ui.clear(elements.getEmailControl());
    }

    public static void setEmail(String value) {
        Ui.setValueBy(elements.getEmailControl(), value);
    }

    public static void clearPhone() {
        Ui.clear(elements.getPhoneControl());
    }

    public static void setPhone(String value) {
        Ui.setValueBy(elements.getPhoneControl(), value);
    }

    public static void clearSocialLink() {
        Ui.clear(elements.getSocialLinkControl());
    }

    public static void setSocialLink(String value) {
        Ui.setValueBy(elements.getSocialLinkControl(), value);
    }
}
