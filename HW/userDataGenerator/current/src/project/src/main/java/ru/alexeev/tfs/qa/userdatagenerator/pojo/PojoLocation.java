package ru.alexeev.tfs.qa.userdatagenerator.pojo;


import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeNumber;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeText;


public class PojoLocation {

    private static final AttributeNumber emptyHouse = null;
    private static final AttributeNumber emptyApartment = null;

    private final int postcode;
    private final String state;
    private final String city;
    private final String street;

    public PojoLocation(int postcode, String state, String city, String street) {
        this.postcode = postcode;
        this.state = state;
        this.city = city;
        this.street = street;
    }

    public AttributeNumber getPostalIndex() {
        return new AttributeNumber(this.postcode);
    }

    public AttributeText getRegion() {
        return new AttributeText(this.state);
    }

    public AttributeText getCity() {
        return new AttributeText(this.city);
    }

    public AttributeText getStreet() {
        return new AttributeText(this.street);
    }

    public AttributeNumber getHouse() {
        return emptyHouse;
    }

    public AttributeNumber getApartment() {
        return emptyApartment;
    }
}
