package comp4240.kanonymity.attribute;

public abstract class Attribute {
    protected AttributeType attributeType;

    public void setAttributeType(AttributeType attributeType) { this.attributeType = attributeType; }
    public AttributeType getAttributeType() { return this.attributeType; }
}