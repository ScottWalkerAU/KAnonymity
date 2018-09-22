package comp4240.kanonymity.attribute;

import static comp4240.kanonymity.attribute.AttributeType.BINARY;

public class BinaryAttribute extends Attribute {

    private boolean value;

    public BinaryAttribute(boolean value, IdentifierType identifierType) {
        super(BINARY, identifierType);
        setValue(value);
    }

    public BinaryAttribute(String value, IdentifierType identifierType) {
        super(BINARY, identifierType);
        setValue(value);
    }

    public boolean getValue() {
        return this.value;
    }

    public void setValue(String value) {
        // TODO This is broken
        String defaults = "male, m, y, yes, true, t, 1";
        setValue(defaults.contains(value.toLowerCase()));
    }

    public void setValue(boolean value) {
        this.value = value;
        setModifiedValue(Boolean.toString(value));
    }

    public String toString() {
        return Boolean.toString(value);
    }
}
