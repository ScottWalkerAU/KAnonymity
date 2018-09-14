package comp4240.kanonymity.tree;

public abstract class Tree {
    protected Node root = null;
    protected String attributeHeader;

    public Tree(String attributeHeader) {
        this.attributeHeader = attributeHeader;
    }

    // ----- Getters -----

    public Node getRoot() {
        return root;
    }

    public int getTreeHeight() {
        return root.getHeight();
    }

    public abstract String getGeneralised(String value, int suppressionLevel);

    public String getAttributeHeader() {
        return attributeHeader;
    }

    // ----- Setters -----
    public void setRoot(Node root) {
        this.root = root;
    }

    public void setAttributeHeader(String attributeHeader) {
        this.attributeHeader = attributeHeader;
    }
}
