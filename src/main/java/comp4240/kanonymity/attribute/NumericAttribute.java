package comp4240.kanonymity.attribute;

import static comp4240.kanonymity.attribute.AttributeType.NUMERIC;

public class NumericAttribute extends Attribute {

    private double value;

    public NumericAttribute(double value, IdentifierType identifierType) {
        super(NUMERIC, identifierType);
        setValue(value);
    }

    public boolean equivalentTo(Attribute other) {
        if (!(other instanceof NumericAttribute)) {
            return false;
        }

        // TODO Leeway on double comparison for a small delta?
        double otherValue = ((NumericAttribute) other).getValue();
        return value == otherValue;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
        setModifiedValue(Double.toString(value));
    }

    public String toString() {
        return Double.toString(value);
    }
}
