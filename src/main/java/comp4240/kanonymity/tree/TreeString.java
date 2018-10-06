package comp4240.kanonymity.tree;

import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.attribute.StringAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TreeString extends Tree {

    private HashMap<String, Node<String>> nodes = new HashMap<>();

    public TreeString(String attributeHeader) {
        super(attributeHeader);
    }

    /**
     * Add the children to the parent node. If any of the Nodes already exsist use those, otherwise create new nodes.
     * @param parent
     * @param children
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
     * @param value
     * @return
     */
    private Node<String> findNode(String value) {
        return nodes.get(value);
    }

    public void addNode(Node<String> node) {
        nodes.put(node.toString(), node);
    }


    // -- Getters --

    /**
     * Returns the generalised value of an attribute by getting the nodes parent 'suppressionLevel' times.
     * @param value
     * @param generalisationLevel
     * @return
     */
    public String getGeneralised(String value, int generalisationLevel) {
        // If the node doesn't exist, throw an error
        Node node = findNode(value);
        if (node == null) {
            throw new IllegalArgumentException("Cannot find node with value '" + value + "' in the taxonomy tree '" + attributeHeader + "'");
        }

        return getGeneralisedNode(node, generalisationLevel).toString();
    }

    public List<Node> getNodes() {
        return new ArrayList<>(nodes.values());
    }

    public Node getNode(Attribute attribute) {
        StringAttribute attr = (StringAttribute) attribute; // TODO unchecked cast
        return findNode(attr.getValue());
    }
}
