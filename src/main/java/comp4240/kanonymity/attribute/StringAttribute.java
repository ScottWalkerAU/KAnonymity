package comp4240.kanonymity.attribute;

import static comp4240.kanonymity.attribute.AttributeType.STRING;

public class StringAttribute extends Attribute {

    private String value;

    public StringAttribute(String value, IdentifierType identifierType) {
        super(STRING, identifierType);
        setValue(value);
        setModifiedValue(value);
    }

    public boolean equivalentTo(Attribute other) {
        if (!(other instanceof StringAttribute)) {
            return false;
        }

        String otherValue = ((StringAttribute) other).getValue();
        return value.equals(otherValue);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
