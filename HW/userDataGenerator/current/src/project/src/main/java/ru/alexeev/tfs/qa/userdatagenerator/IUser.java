package ru.alexeev.tfs.qa.userdatagenerator;


import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeDate;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeInn;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeNumber;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeText;


public interface IUser {
    AttributeText getFirstName();
    AttributeText getLastName();
    AttributeText getPatronicName();
    AttributeNumber getAge();
    AttributeText getGender();
    AttributeDate getBirthDate();
    AttributeInn getInn();
    AttributeNumber getPostalIndex();
    AttributeText getCountry();
    AttributeText getRegion();
    AttributeText getCity();
    AttributeText getStreet();
    AttributeNumber getHouse();
    AttributeNumber getApartment();
}
