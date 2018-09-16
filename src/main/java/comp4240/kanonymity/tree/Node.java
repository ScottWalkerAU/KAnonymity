package comp4240.kanonymity.tree;

import java.util.*;

public abstract class Node {

    private Node parent;
    private List<Node> children;
    private int level;

    public Node(Node parent) {
        this.parent = parent;
        this.children = new ArrayList<>();
        this.level = parent == null ? 0 : parent.getLevel() + 1;
    }

    /**
     * Add one or more children to the node.
     * @param nodes Children to be added
     */
    public void addChild(Node... nodes) {
        Collections.addAll(children, nodes);
    }

    // -- Abstract methods --

    public abstract String getValue();

    // -- Getters --

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public int getLevel() {
        return level;
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

    // -- Misc --

    @Override
    public String toString() {
        return getValue();
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