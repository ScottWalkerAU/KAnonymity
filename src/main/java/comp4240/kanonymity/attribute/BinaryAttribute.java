package comp4240.kanonymity.attribute;

import static comp4240.kanonymity.attribute.AttributeType.BINARY;

public class BinaryAttribute extends Attribute {
    private boolean value;

    public BinaryAttribute(boolean value, IdentifierType identifierType) {
        setValue(value);
        setAttributeType(BINARY);
        setIdentifierType(identifierType);
    }

    public BinaryAttribute(String value, IdentifierType identifierType) {
        setValue(value);
        setAttributeType(BINARY);
        setIdentifierType(identifierType);
    }

    public boolean getValue() { return this.value; }
    public void setValue(boolean value) { this.value = value; }
    public void setValue(String value) {
        String defaults = "male, m, y, yes, true, t, 1";

        if (defaults.contains(value)) {
            this.value = true;
        } else {
            this.value = false;
        }
    }
    public String toString() { return value + ""; }

    public boolean equivalentTo(Attribute other) {
        if (other instanceof BinaryAttribute) {
            return value == ((BinaryAttribute) other).getValue();
        } else {
            return false;
        }
    }
}
