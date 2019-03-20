public class AttributeNumber extends Attribute {

    private Integer value;

    public AttributeNumber(int number) {
        this.value = number;
    }

    public AttributeNumber(Integer number) {
        this.value = number;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}
