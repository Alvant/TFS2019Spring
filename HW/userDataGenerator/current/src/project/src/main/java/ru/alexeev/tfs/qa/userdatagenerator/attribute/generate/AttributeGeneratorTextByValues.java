package ru.alexeev.tfs.qa.userdatagenerator.attribute.generate;


import ru.alexeev.tfs.qa.userdatagenerator.Utils;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.AttributeText;


public class AttributeGeneratorTextByValues extends AttributeGenerator {

    private String[] values;

    public AttributeGeneratorTextByValues() { super(); }

    @Override
    public void initializeValues(Object[] values) {
        this.values = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            this.values[i] = (String) values[i];
        }
    }

    @Override
    public Object generate() {
        int index = Utils.randomInt(this.values.length);
        String text = this.values[index];
        AttributeText result = new AttributeText(text);

        return (Object) result;
    }
}
