package comp4240.kanonymity.attribute;

import static comp4240.kanonymity.attribute.AttributeType.BINARY;

public class BinaryAttribute extends Attribute {
    private boolean value;

    public BinaryAttribute(boolean value) {
        setValue(value);
        setAttributeType(BINARY);
    }

    public BinaryAttribute(String value) {
        setValue(value);
        setAttributeType(BINARY);
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
}
