package comp4240.kanonymity.attribute;

public abstract class Attribute {
    protected IdentifierType identifierType;
    protected AttributeType attributeType;

    public abstract boolean equivalentTo(Attribute other);
    public void setAttributeType(AttributeType attributeType) { this.attributeType = attributeType; }
    public AttributeType getAttributeType() { return this.attributeType; }
    public void setIdentifierType(IdentifierType identifierType) { this.identifierType = identifierType; }
    public IdentifierType geIdentifierType() { return this.identifierType; }

}