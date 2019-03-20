package ru.alexeev.tfs.qa.userdatagenerator.attribute.generate;


public class AttributeGenerator implements IAttributeGenerator {

    public AttributeGenerator() {}

    public Object generate() {
        throw new UnsupportedOperationException(
                "Method generate() should be implemented in child class");
    }

    public Object generate(Object parameter) {
        throw new UnsupportedOperationException(
                "Method generate() should be implemented in child class");
    }

    public void initializeRange(Object edgeLeft, Object edgeRight) {
        throw new UnsupportedOperationException(
                "Method initializeRange() should be implemented in child class");
    }

    public void initializeValues(Object[] values) {
        throw new UnsupportedOperationException(
                "Method initializeValues() should be implemented in child class");
    }
}