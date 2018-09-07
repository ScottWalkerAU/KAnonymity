package comp4240.kanonymity.attribute;

import static comp4240.kanonymity.attribute.AttributeType.NUMERIC;

public class NumericAttribute extends Attribute {
    private double value;

    public NumericAttribute(double value, IdentifierType identifierType) {
        super(NUMERIC, identifierType);
        setValue(value);
    }

    public double getValue() { return this.value; }
    public void setValue(double value) { this.value = value; }
    public String toString() { return value + ""; }

    public boolean equivalentTo(Attribute other) {
        if (other instanceof NumericAttribute) {
            return value == ((NumericAttribute) other).getValue();
        } else {
            return false;
        }
    }
}
