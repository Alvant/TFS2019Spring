package ru.alexeev.tfs.qa.userdatagenerator.attribute;


public class AttributeInn extends Attribute {

    private static int length = 12;
    private int[] numbers;

    public AttributeInn(int[] numbers) {
        this.initializeNumbers(numbers);
    }

    public String toString() {
        String result = "";

        for (int number : this.numbers) {
            result += Integer.toString(number);
        }

        return result;
    }

    private void initializeNumbers(int[] numbers) {
        this.numbers = new int[numbers.length];

        if (numbers.length != AttributeInn.length) {
            throw new IllegalArgumentException(
                    String.format("Invalid array size \"%s\". Expected \"%s\"", numbers.length, AttributeInn.length));
        }

        for (int i = 0; i < numbers.length; i++) {
            this.numbers[i] = numbers[i];
        }
    }
}
