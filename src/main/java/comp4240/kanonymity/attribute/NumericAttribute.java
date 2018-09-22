package comp4240.kanonymity.attribute;

import static comp4240.kanonymity.attribute.AttributeType.NUMERIC;

public class NumericAttribute extends Attribute {

    private Integer value;

    public NumericAttribute(Integer value, IdentifierType identifierType) {
        super(NUMERIC, identifierType);
        setValue(value);
    }

    public boolean equivalentTo(Attribute other) {
        if (!(other instanceof NumericAttribute)) {
            return false;
        }

        double otherValue = ((NumericAttribute) other).getValue();
        return value == otherValue;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
        setModifiedValue(Double.toString(value));
    }

    public String toString() {
        return Integer.toString(value);
    }
}
