package comp4240.kanonymity.attribute;

public class StringAttribute extends Attribute {
    private String value;

    public StringAttribute(String value, IdentifierType identifierType) {
        setValue(value);
        setAttributeType(AttributeType.STRING);
        setIdentifierType(identifierType);
    }

    public String getValue() { return this.value; }
    public void setValue(String value) { this.value = value; }
    public String toString() { return value; }

    public boolean equivalentTo(Attribute other) {
        if (other instanceof StringAttribute) {
            String otherValue = ((StringAttribute) other).getValue();
            return value.equals(otherValue);
        } else {
            return false;
        }
    }
}
