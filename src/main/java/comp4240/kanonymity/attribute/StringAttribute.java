package comp4240.kanonymity.attribute;

public class StringAttribute extends Attribute {
    private String value;

    public StringAttribute(String value) {
        setValue(value);
        setAttributeType(AttributeType.STRING);
    }

    public String getValue() { return this.value; }
    public void setValue(String value) { this.value = value; }
    public String toString() { return value; }
}
