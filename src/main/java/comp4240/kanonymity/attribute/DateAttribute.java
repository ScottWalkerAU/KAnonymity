package comp4240.kanonymity.attribute;

import static comp4240.kanonymity.attribute.AttributeType.DATE;

public class DateAttribute extends Attribute {
    private String value;

    public DateAttribute(String value, IdentifierType identifierType) {
        super(DATE, identifierType);
        setValue(value);
    }

    public String getValue() { return this.value; }
    public void setValue(String value) { this.value = value; }
    public String toString() { return value + ""; }

    public boolean equivalentTo(Attribute other) {
        return false;
    }
}
