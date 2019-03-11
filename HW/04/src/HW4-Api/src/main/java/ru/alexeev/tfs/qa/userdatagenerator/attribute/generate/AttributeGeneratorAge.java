package ru.alexeev.tfs.qa.userdatagenerator.attribute.generate;


import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeDate;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeNumber;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class AttributeGeneratorAge extends AttributeGenerator {

    private Calendar currentCalendar = new GregorianCalendar();
    private static int ageDefault = 0;

    public AttributeGeneratorAge() {}

    @Override
    public Object generate() {
        return (Object) new AttributeNumber(AttributeGeneratorAge.ageDefault);
    }

    @Override
    public Object generate(Object parameter) {
        int age = this.computeAge((AttributeDate) parameter);

        return (Object) new AttributeNumber(age);
    }

    private int computeAge(AttributeDate attributeDate) {
        Calendar birthCalendar = attributeDate.getDate();

        int birthYear = birthCalendar.get(Calendar.YEAR);
        int currentYear = this.currentCalendar.get(Calendar.YEAR);

        int age = currentYear - birthYear;

        if (age < 0) {
            throw new IllegalArgumentException(
                    String.format("Birth year \"%s\" is bigger than current year \"%s\"", birthYear, currentYear));
        }

        return age;
    }
}
