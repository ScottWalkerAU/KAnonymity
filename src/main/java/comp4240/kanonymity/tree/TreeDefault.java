package comp4240.kanonymity.tree;

import java.util.LinkedList;
import java.util.List;

public class TreeDefault extends Tree {
    private List<Node> nodes = new LinkedList<>();

    /**
     * Add the children to the parent node. If any of the Nodes already exsist use those, otherwise create new nodes.
     * @param parent
     * @param children
     */
    public void add(String parent, String... children) {
        // If the node already exists collect it
        Node attribute = findNode(parent);

        // Otherwise create a new node and add it to the list of nodes.
        if (attribute == null) {
            attribute = new Node(parent);
            nodes.add(attribute);
        }

        // For all children passed in
        for (String c : children) {
            // If the node already exists collect it
            Node n = findNode(c);

            // Otherwise create a new node and add it to the list of nodes.
            if (n == null) {
                n = new Node(c);
                nodes.add(n);
            }

            // Add the child to the parent node
            attribute.addChild(n);
            // Set the parent node for the child
            n.setParent(attribute);
        }

        // First time set the root node.
        if (root == null) {
            root = attribute;
        }
    }

    /**
     * Look for the node within the current array of recorded nodes.
     *
     * @param value
     * @return
     */
    private Node findNode(String value) {
        for (Node n : nodes) {
            if (n.getValue().equals(value)) {
                return n;
            }
        }

        return null;
    }

    // ----- Getters -----

    /**
     * Returns the generalised value of an attribute by getting the nodes parent 'suppressionLevel' times.
     *
     * @param value
     * @param suppressionLevel
     * @return
     */
    public String getGeneralised(String value, int suppressionLevel) {
        // Ensure the suppression level is positive
        assert(suppressionLevel > 0);

        // Get the current node from the value
        Node n = findNode(value);

        // If it doesnt exist return null
        if (n == null) {
            return null;
        }

        // Otherwise get the parent of the node 'suppressionLevel' times
        for (int i = 0; i < suppressionLevel; i++) {
            n = n.getParent();

            // If the node is reached return the root value
            if (n == root) {
                return root.getValue();
            }
        }

        return n.getValue();
    }
}
