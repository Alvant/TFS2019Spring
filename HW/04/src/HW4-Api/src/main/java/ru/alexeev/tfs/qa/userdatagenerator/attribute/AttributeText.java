package ru.alexeev.tfs.qa.userdatagenerator.attribute;


public class AttributeText extends Attribute {

    private String value;

    public AttributeText(String text) {
        this.value = text;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
