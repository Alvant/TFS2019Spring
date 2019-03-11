package ru.alexeev.tfs.qa.userdatagenerator.pojo;


import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeText;


public class PojoName {

    private static final AttributeText emptyPatronicName = null;

    private final String first;
    private final String last;

    public PojoName(String first, String last) {
        this.first = first;
        this.last = last;
    }

    public AttributeText getFirstName() {
        return new AttributeText(this.first);
    }

    public AttributeText getLastName() {
        return new AttributeText(this.last);
    }

    public AttributeText getPatronicName() {
        return emptyPatronicName;
    }
}
