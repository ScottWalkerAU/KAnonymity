package comp4240.kanonymity.attribute;

public abstract class Attribute {

    private AttributeType attributeType;
    private IdentifierType identifierType;

    public Attribute(AttributeType attributeType, IdentifierType identifierType) {
        setAttributeType(attributeType);
        setIdentifierType(identifierType);
    }

    // -- Abstract methods --

    public abstract boolean equivalentTo(Attribute other);

    // -- Getters and Setters --

    public AttributeType getAttributeType() {
        return this.attributeType;
    }

    private void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public IdentifierType getIdentifierType() {
        return this.identifierType;
    }

    private void setIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
    }
}