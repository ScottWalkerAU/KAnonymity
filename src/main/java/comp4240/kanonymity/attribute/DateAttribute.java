package comp4240.kanonymity.attribute;

public class DateAttribute extends Attribute {
    private String value;

    public DateAttribute(String value, IdentifierType identifierType) {
        setValue(value);
        setAttributeType(AttributeType.DATE);
        setIdentifierType(identifierType);
    }

    public String getValue() { return this.value; }
    public void setValue(String value) { this.value = value; }
    public String toString() { return value + ""; }

    public boolean equivalentTo(Attribute other) {
        return false;
    }
}
