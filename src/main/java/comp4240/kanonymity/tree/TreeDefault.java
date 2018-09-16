package comp4240.kanonymity.tree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TreeDefault extends Tree {

    private HashMap<String, Node> nodes = new HashMap<>();

    public TreeDefault(String attributeHeader) {
        super(attributeHeader);
    }

    /**
     * Add the children to the parent node. If any of the Nodes already exsist use those, otherwise create new nodes.
     * @param parent
     * @param children
     */
    public void add(String parent, String... children) {
        Node node;

        // First time set the root node
        if (root == null) {
            node = new Node(parent);
            root = node;
            addNode(root);
        } else {
            node = findNode(parent);
        }

        // Otherwise create a new node and add it to the list of nodes.
        if (node == null) {
            throw new IllegalArgumentException("The parent must already be defined in the taxonomy tree");
        }

        // For all children passed in
        for (String c : children) {
            Node child = new Node(node, c);
            node.addChild(child);
            addNode(child);
        }

    }

    /**
     * Look for the node within the current array of recorded nodes.
     * @param value
     * @return
     */
    private Node findNode(String value) {
        return nodes.get(value);
    }

    public void addNode(Node node) {
        nodes.put(node.getValue(), node);
    }

    // -- Getters --

    /**
     * Returns the generalised value of an attribute by getting the nodes parent 'suppressionLevel' times.
     * @param value
     * @param generalisationLevel
     * @return
     */
    public String getGeneralised(String value, int generalisationLevel) {
        // Ensure the suppression level is positive
        if (generalisationLevel < 0) {
            return value;
        }

        // Get the current node from the value
        Node n = findNode(value);

        // If it doesnt exist return null
        if (n == null) {
            return null;
        }

        // Otherwise get the parent of the node 'suppressionLevel' times
        for (int i = 0; i < generalisationLevel; i++) {
            n = n.getParent();

            // If the node is reached return the root value
            if (n == root) {
                return root.getValue();
            }
        }

        return n.getValue();
    }
}
