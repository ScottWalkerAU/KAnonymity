package comp4240.kanonymity.tree;

import java.util.HashMap;

public class TreeDefault extends Tree {

    private HashMap<String, NodeString> nodes = new HashMap<>();

    public TreeDefault(String attributeHeader) {
        super(attributeHeader);
    }

    /**
     * Add the children to the parent node. If any of the Nodes already exsist use those, otherwise create new nodes.
     * @param parent
     * @param children
     */
    public void add(String parent, String... children) {
        NodeString node;

        // First time set the root node
        if (root == null) {
            node = new NodeString(parent);
            root = node;
            addNode(node);
        } else {
            node = findNode(parent);
        }

        // Otherwise create a new node and add it to the list of nodes.
        if (node == null) {
            throw new IllegalArgumentException("The parent must already be defined in the taxonomy tree");
        }

        // For all children passed in
        for (String c : children) {
            NodeString child = new NodeString(node, c);
            node.addChild(child);
            addNode(child);
        }

    }

    /**
     * Look for the node within the current array of recorded nodes.
     * @param value
     * @return
     */
    private NodeString findNode(String value) {
        return nodes.get(value);
    }

    public void addNode(NodeString node) {
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
        // If the node doesn't exist, throw an error
        Node node = findNode(value);
        if (node == null) {
            throw new IllegalArgumentException("Cannot find node with value " + value + " in the taxonomy tree");
        }

        return getGeneralisedNode(node, generalisationLevel).getValue();
    }
}
