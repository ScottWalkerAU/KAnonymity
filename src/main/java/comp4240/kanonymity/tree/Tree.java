package comp4240.kanonymity.tree;

import java.util.ArrayList;
import java.util.List;

public abstract class Tree {

    protected Node root = null;
    protected String attributeHeader;

    public Tree(String attributeHeader) {
        this.attributeHeader = attributeHeader;
    }

    // -- Abstract methods --

    public abstract String getGeneralised(String value, int generalisationLevel);

    // -- Main methods --

    protected Node getGeneralisedNode(Node node, int generalisationLevel) {
        // Ensure the suppression level is positive
        if (generalisationLevel < 0) {
            generalisationLevel = 0;
        }

        // How many times we want to go up in the tree to get to the generalisation level
        int ascensions = node.getLevel() - generalisationLevel;

        // Perform the ascensions
        for (int i = 0; i < ascensions; i++) {
            node = node.getParent();

            // If the node is reached return the root value
            if (node == root) {
                break;
            }
        }
        return node;
    }


    /**
     * Given a nodes value, return a list of all the values of the subtree nodes.
     *
     * @param node     The root node of the subtree.
     */
    public abstract List<String> getSubtree(String value);

    // -- Getters --

    public Node getRoot() {
        return root;
    }

    public int getTreeHeight() {
        return root.getHeight();
    }

    public String getAttributeHeader() {
        return attributeHeader;
    }

    // -- Setters --
    public void setRoot(Node root) {
        this.root = root;
    }

    public void setAttributeHeader(String attributeHeader) {
        this.attributeHeader = attributeHeader;
    }
}
