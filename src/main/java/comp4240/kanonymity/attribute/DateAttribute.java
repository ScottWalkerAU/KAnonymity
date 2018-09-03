package comp4240.kanonymity.attribute;

public class DateAttribute extends Attribute {
    private String value;

    public DateAttribute(String value) {
        setValue(value);
        setAttributeType(AttributeType.DATE);
    }

    public String getValue() { return this.value; }
    public void setValue(String value) { this.value = value; }
    public String toString() { return value + ""; }
}
