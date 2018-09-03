package comp4240.kanonymity.attribute;

import static comp4240.kanonymity.attribute.AttributeType.NOMINAL;

public class NominalAttribute extends Attribute {
    private double value;

    public NominalAttribute(double value, IdentifierType identifierType) {
        setValue(value);
        setAttributeType(NOMINAL);
        setIdentifierType(identifierType);
    }

    public double getValue() { return this.value; }
    public void setValue(double value) { this.value = value; }
    public String toString() { return value + ""; }

    public boolean equivalentTo(Attribute other) {
        if (other instanceof NominalAttribute) {
            return value == ((NominalAttribute) other).getValue();
        } else {
            return false;
        }
    }
}
