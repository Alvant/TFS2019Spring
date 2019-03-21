package ru.alexeev.tfs.qa.userdatagenerator.database;


import ru.alexeev.tfs.qa.userdatagenerator.Constants;
import ru.alexeev.tfs.qa.userdatagenerator.IUser;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeDate;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeInn;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeNumber;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DatabaseUser implements IUser {
    protected String surname;
    protected String name;
    protected String middlename;
    protected Date birthdate;
    protected String gender;
    protected String inn;

    private DatabaseAddress address;

    private static final String sexMale = "M";
    private static final String sexFemale = "F";

    public DatabaseUser(
            String surname, String name, String middlename,
            Date birthdate, String gender, String inn) {

        this.surname = surname;
        this.name = name;
        this.middlename = middlename;
        this.birthdate = birthdate;
        this.gender = gender;
        this.inn = inn;
    }

    public static String getSexMale() {
        return sexMale;
    }

    public static String getSexFemale() {
        return sexFemale;
    }

    public void setAddress(DatabaseAddress address) {
        this.address = address;
    }

    public String convertDateToStringInDatabaseFormat() {
        DateFormat df = new SimpleDateFormat(DatabaseHandler.javaDateToStringConvertFormat);
        String dateString = df.format(this.birthdate);

        return dateString;
    }

    public Date convertStringInDatabaseFormatToDate(String dateString) throws java.text.ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DatabaseHandler.javaDateToStringConvertFormat);
        Date date = sdf.parse(dateString);

        return date;
    }

    public AttributeText getFirstName() {
        return new AttributeText(this.name);
    }

    public AttributeText getLastName() {
        return new AttributeText(this.surname);
    }

    public AttributeText getPatronicName() {
        if (this.middlename == null) {
            return null;
        }

        return new AttributeText(this.middlename);
    }

    public AttributeNumber getAge() {
        Date currentDate = new Date();

        // TODO: improve computation
        int daysInYearCount = 365;
        long differenceMs = Math.abs(currentDate.getTime() - this.birthdate.getTime());
        long differenceDays = TimeUnit.DAYS.convert(differenceMs, TimeUnit.MILLISECONDS);

        return new AttributeNumber((int) differenceDays / daysInYearCount);
    }

    public AttributeText getGender() {
        return new AttributeText(this.formatGender(this.gender));
    }

    public AttributeDate getBirthDate() {
        try {
            DateFormat df = new SimpleDateFormat(DatabaseHandler.javaDateToStringConvertFormat);
            String dateString = df.format(this.birthdate);
            return new AttributeDate(dateString, DatabaseHandler.javaDateToStringConvertFormat);
        } catch (ParseException ex) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.birthdate);
            return new AttributeDate(calendar);
        }
    }

    public AttributeInn getInn() {
        if (this.inn == null) {
            return null;
        }

        int[] numbers = new int[this.inn.length()];

        for (int i = 0; i < this.inn.length(); i++) {
            numbers[i] = Integer.parseInt(Character.toString(this.inn.charAt(i)));
        }

        return new AttributeInn(numbers);
    }

    public AttributeNumber getPostalIndex() {
        return this.address.getPostalIndex();
    }

    public AttributeText getCountry() {
        return this.address.getCountry();
    }

    public AttributeText getRegion() {
        return this.address.getRegion();
    }

    public AttributeText getCity() {
        return this.address.getCity();
    }

    public AttributeText getStreet() {
        return this.address.getStreet();
    }

    public AttributeNumber getHouse() {
        return this.address.getHouse();
    }

    public AttributeNumber getApartment() {
        return this.address.getApartment();
    }

    private String formatGender(String genderFromDatabase) {
        if (genderFromDatabase == sexFemale) {
            return Constants.sexValues.get("female");
        } else if (genderFromDatabase == sexMale) {
            return Constants.sexValues.get("male");
        } else {
            return Constants.undefinedSexValue;
        }
    }
}
