package ru.alexeev.tfs.qa.userdatagenerator.database;


import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeNumber;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeText;


public class DatabaseAddress {
    protected String postcode;
    protected String country;
    protected String region;
    protected String city;
    protected String street;
    protected int house;
    protected int flat;

    public DatabaseAddress(
            String postcode, String country, String region,
            String city, String street,
            int house, int flat) {

        this.postcode = postcode;
        this.country = country;
        this.region = region;
        this.city = city;
        this.street = street;
        this.house = house;
        this.flat = flat;
    }

    public AttributeNumber getPostalIndex() {
        return new AttributeNumber(Integer.parseInt(this.postcode));
    }

    public AttributeText getCountry() {
        return new AttributeText(this.country);
    }

    public AttributeText getRegion() {
        return new AttributeText(this.region);
    }

    public AttributeText getCity() {
        return new AttributeText(this.city);
    }

    public AttributeText getStreet() {
        return new AttributeText(this.street);
    }

    public AttributeNumber getHouse() {
        return new AttributeNumber(this.house);
    }

    public AttributeNumber getApartment() {
        return new AttributeNumber(this.flat);
    }
}
