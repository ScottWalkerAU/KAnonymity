package comp4240.kanonymity.attribute;

public abstract class Attribute {

    private AttributeType attributeType;
    private IdentifierType identifierType;
    private String modifiedValue; // Where the generalised value is stored

    public Attribute(AttributeType attributeType, IdentifierType identifierType) {
        setAttributeType(attributeType);
        setIdentifierType(identifierType);
    }

    // -- General Methods --

    public final boolean equivalentTo(Attribute other) {
        if (other == null) {
            return false;
        }

        if (this.getClass() != other.getClass()) {
            return false;
        }

        return getModifiedValue().equals(other.getModifiedValue());
    }

    public void resetModifiedValue() {
        modifiedValue = toString();
    }

    // -- Getters and Setters --

    public AttributeType getAttributeType() {
        return this.attributeType;
    }

    public IdentifierType getIdentifierType() {
        return this.identifierType;
    }

    public String getModifiedValue() {
        return this.modifiedValue;
    }

    private void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    private void setIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
    }

    public void setModifiedValue(String modifiedValue) {
        this.modifiedValue = modifiedValue;
    }

}