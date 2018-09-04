package comp4240.kanonymity.attribute;

public abstract class Attribute {

    private IdentifierType identifierType;
    private AttributeType attributeType;

    public Attribute(AttributeType attributeType, IdentifierType identifierType) {
        this.identifierType = identifierType;
        this.attributeType = attributeType;
    }

    // -- Abstract methods --

    public abstract boolean equivalentTo(Attribute other);

    // -- Getters and Setters --

    public AttributeType getAttributeType() {
        return this.attributeType;
    }

    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public void setIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
    }

    public IdentifierType geIdentifierType() {
        return this.identifierType;
    }
}