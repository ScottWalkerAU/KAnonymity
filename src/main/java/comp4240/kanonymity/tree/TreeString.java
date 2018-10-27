package comp4240.kanonymity.tree;

import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.attribute.StringAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Tree to hold strings
 */
public class TreeString extends Tree {

    private HashMap<String, Node<String>> nodes = new HashMap<>();

    public TreeString(String attributeHeader) {
        super(attributeHeader);
    }

    /**
     * Add the children to the parent node. If any of the Nodes already exist use those, otherwise create new nodes.
     * @param parent Parent string
     * @param children Children strings
     */
    public void add(String parent, String... children) {
        Node<String> node;

        // First time set the root node
        if (root == null) {
            node = new Node<>(parent);
            root = node;
            addNode(node);
        } else {
            node = findNode(parent);
        }

        // Otherwise create a new node and add it to the list of nodes.
        if (node == null) {
            throw new IllegalArgumentException("The parent must already be defined in the taxonomy tree, couldn't find '" + parent + "'");
        }

        // For all children passed in
        for (String c : children) {
            Node<String> child = new Node<>(node, c);
            node.addChild(child);
            addNode(child);
        }

    }

    /**
     * Look for the node within the current array of recorded nodes.
     * @param value Value to find
     * @return The node containing the value
     */
    private Node<String> findNode(String value) {
        return nodes.get(value);
    }

    /**
     * Add the node to the list
     * @param node Node to add
     */
    private void addNode(Node<String> node) {
        nodes.put(node.toString(), node);
    }


    // -- Getters --

    /**
     * Returns the generalised value of an attribute by getting the nodes parent 'generalisationLevel' times.
     * @param value Value to generalise
     * @param generalisationLevel Level to read
     * @return Resultant value
     */
    public String getGeneralised(String value, int generalisationLevel) {
        // If the node doesn't exist, throw an error
        Node node = findNode(value);
        if (node == null) {
            throw new IllegalArgumentException("Cannot find node with value '" + value + "' in the taxonomy tree '" + attributeHeader + "'");
        }

        return getGeneralisedNode(node, generalisationLevel).toString();
    }

    /**
     * @return All nodes in the tree
     */
    public List<Node> getNodes() {
        return new ArrayList<>(nodes.values());
    }

    /**
     * Find the node given the attribute
     * @param attribute Attribute
     * @return Node
     */
    public Node getNode(Attribute attribute) {
        StringAttribute attr = (StringAttribute) attribute; // TODO unchecked cast
        return findNode(attr.getValue());
    }
}
