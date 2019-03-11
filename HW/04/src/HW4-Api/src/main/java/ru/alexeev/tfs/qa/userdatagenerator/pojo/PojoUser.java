package ru.alexeev.tfs.qa.userdatagenerator.pojo;


import com.google.gson.Gson;
import ru.alexeev.tfs.qa.userdatagenerator.Constants;
import ru.alexeev.tfs.qa.userdatagenerator.IUser;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeDate;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeInn;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeNumber;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeText;


public final class PojoUser implements IUser {

    private static final AttributeInn emptyInn = null;

    private final PojoName name;
    private final String gender;
    private final PojoLocation location;
    private final PojoDateOfBirth dob;
    private final String nat;

    public static PojoUser[] toArray(String json) {
        return new Gson().fromJson(json, PojoUser[].class);
    }

    public static PojoUser fromJson(String json) {
        return new Gson().fromJson(json, PojoUser.class);
    }

    public PojoUser(PojoName name, String gender, PojoLocation location, PojoDateOfBirth dob, String nat) {
        this.name = name;
        this.gender = gender;
        this.location = location;
        this.dob = dob;
        this.nat = nat;
    }

    public AttributeText getFirstName() { return this.name.getFirstName(); }

    public AttributeText getLastName() { return this.name.getLastName(); }

    public AttributeText getPatronicName() { return this.name.getPatronicName(); }

    public AttributeNumber getAge() { return this.dob.getAge(); }

    public AttributeText getGender() {
        return new AttributeText(this.formatGender(this.gender));
    }

    public AttributeDate getBirthDate() {
        return this.dob.getBirthDate();
    }

    public AttributeInn getInn() { return emptyInn; }

    public AttributeNumber getPostalIndex() { return this.location.getPostalIndex(); }

    public AttributeText getCountry() {
        return new AttributeText(this.transformNationalityToCountry(this.nat));
    }

    public AttributeText getRegion() { return this.location.getRegion(); }

    public AttributeText getCity() { return this.location.getCity(); }

    public AttributeText getStreet() { return this.location.getStreet(); }

    public AttributeNumber getHouse() { return this.location.getHouse(); }

    public AttributeNumber getApartment() { return this.location.getApartment(); }

    private String formatGender(String genderFromApi) {
        String genderInLowerCase = genderFromApi.toLowerCase();

        if (genderInLowerCase == "female") {
            return Constants.sexValues.get("female");
        } else if (genderInLowerCase == "male") {
            return Constants.sexValues.get("male");
        } else {
            return Constants.undefinedSexValue;
        }
    }

    private String transformNationalityToCountry(String nationality) {
        String nationalityInLowerCase = nationality.toLowerCase();

        if (nationalityInLowerCase == "us") {
            return "USA";
        } else {
            return Constants.undefinedCountry;
        }
    }
}

