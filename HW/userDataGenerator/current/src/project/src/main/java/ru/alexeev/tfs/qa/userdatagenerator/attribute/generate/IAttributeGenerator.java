package ru.alexeev.tfs.qa.userdatagenerator.attribute.generate;


public interface IAttributeGenerator {

    Object generate();
    Object generate(Object parameter);
}
