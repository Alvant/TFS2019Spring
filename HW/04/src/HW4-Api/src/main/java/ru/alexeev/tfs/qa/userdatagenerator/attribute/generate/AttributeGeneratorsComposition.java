package ru.alexeev.tfs.qa.userdatagenerator.attribute.generate;


import java.util.HashMap;
import java.util.Map;


public class AttributeGeneratorsComposition implements IAttributeGenerator {

    private Map<String, AttributeGenerator> attributeGenerators;

    public AttributeGeneratorsComposition(Map<String, AttributeGenerator> attributeGenerators) {
        this.attributeGenerators = new HashMap<String, AttributeGenerator>();

        for (Map.Entry<String, AttributeGenerator> entry : attributeGenerators.entrySet()) {
            this.attributeGenerators.put(entry.getKey(), entry.getValue());
        }
    }

    public Object generate() {
        throw new UnsupportedOperationException(
                "Method generate() is not supposed to be used in AttributeGeneratorsComposition");
    }

    public Object generate(Object parameter) {
        String key = (String) parameter;

        return this.get(key).generate();  // just generate -- generate(Object) not needed in the task
    }

    public AttributeGenerator get(String key) {
        if (!this.attributeGenerators.containsKey(key)) {
            throw new IllegalArgumentException(
                    String.format("Illegal key \"%s\" passed", key));
        }

        return this.attributeGenerators.get(key);
    }
}
