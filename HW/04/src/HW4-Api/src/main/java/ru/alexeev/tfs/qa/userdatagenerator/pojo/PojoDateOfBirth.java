package ru.alexeev.tfs.qa.userdatagenerator.pojo;


import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeDate;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeNumber;

import java.text.ParseException;


public class PojoDateOfBirth {

    private static final String dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private static final AttributeDate emptyDate = null;

    private final String date;
    private final int age;

    public PojoDateOfBirth(String date, int age) {
        this.date = date;
        this.age = age;
    }

    public AttributeDate getBirthDate() {
        try {
            return new AttributeDate(this.date, dateFormat);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return emptyDate;
        }
    }

    public AttributeNumber getAge() {
        return new AttributeNumber(this.age);
    }
}
