package comp4240.kanonymity.tree;

import comp4240.kanonymity.attribute.Attribute;

import java.util.List;

/**
 * A generalisation ttree
 */
public abstract class Tree {

    /** Root node */
    protected Node root = null;
    /** Name of the attribute */
    protected String attributeHeader;

    /**
     * Constructor
     * @param attributeHeader Name of the attribute
     */
    public Tree(String attributeHeader) {
        this.attributeHeader = attributeHeader;
    }

    // -- Abstract methods --

    public abstract String getGeneralised(String value, int generalisationLevel);

    public abstract Node getNode(Attribute attribute);

    public abstract List<Node> getNodes();

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

    // -- Getters --

    /**
     * @return The height of the tree
     */
    public int getTreeHeight() {
        return root.getHeight();
    }

    /**
     * @return The name of the attribute column
     */
    public String getAttributeHeader() {
        return attributeHeader;
    }
}
