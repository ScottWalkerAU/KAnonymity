package comp4240.kanonymity.tree;

import comp4240.kanonymity.attribute.Attribute;

import java.util.*;

public class Node {
    private Node parent;
    private List<Node> children = new ArrayList<>();
    private Attribute attribute;
    private String value;

    public Node(Attribute attribute) {
        this(attribute, attribute.toString());
    }

    public Node(Attribute attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public Node(String... values) {
        this(null, values[0]);

        for (int i = 1; i < values.length; i++) {
            String value = values[i];
            addChild(new Node(value));
        }
    }

    /**
     * Add one or more children to the node.
     *
     * @param children
     */
    public void addChild(Node... children) {
        for (Node n : children) {
            this.children.add(n);
            n.setParent(this);
        }
    }

    // ----- Getters and Setters -----

    public String getValue() {
        return this.value;
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return this.children;
    }

    /*
        Return the height of the tree from the root the the furthest node.
    */
    public int getHeight() {
        int height = 0;

        for (Node c : children) {
            height = Math.max(height, 1 + c.getHeight());
        }

        return height;
    }

    // ----- Setters -----

    public void setValue(String value) {
        this.value = value;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    // ----- Misc -----

    public String toString() {
        return value;
    }

    public void printAsArray() {
        String out = toString();

        for (Node c : children) {
            out += "," + c.toString();
        }

        if (children.size() > 0) {
            System.out.println(out);
        }

        for (Node c : children) {
            c.printAsArray();
        }
    }
}