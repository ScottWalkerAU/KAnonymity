package comp4240.kanonymity.attribute;

import static comp4240.kanonymity.attribute.AttributeType.NOMINAL;

public class NominalAttribute extends Attribute {
    private double value;

    public NominalAttribute(double value) {
        setValue(value);
        setAttributeType(NOMINAL);
    }

    public double getValue() { return this.value; }
    public void setValue(double value) { this.value = value; }
    public String toString() { return value + ""; }
}
