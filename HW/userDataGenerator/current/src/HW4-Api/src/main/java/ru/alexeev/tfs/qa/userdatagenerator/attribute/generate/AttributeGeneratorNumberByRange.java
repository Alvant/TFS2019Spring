package ru.alexeev.tfs.qa.userdatagenerator.attribute.generate;


import ru.alexeev.tfs.qa.userdatagenerator.Utils;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeNumber;


public class AttributeGeneratorNumberByRange extends AttributeGenerator {

    private Integer edgeLeft;
    private Integer edgeRight;

    public AttributeGeneratorNumberByRange() { super(); }

    @Override
    public void initializeRange(Object edgeLeft, Object edgeRight) {
        this.edgeLeft = (Integer) edgeLeft;
        this.edgeRight = (Integer) edgeRight;
    }

    @Override
    public Object generate() {
        int number = Utils.randomInt(this.edgeLeft, this.edgeRight);
        AttributeNumber result = new AttributeNumber(number);

        return (Object) result;
    }
}
