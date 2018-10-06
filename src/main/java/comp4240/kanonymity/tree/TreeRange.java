package comp4240.kanonymity.tree;

import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.attribute.NumericAttribute;

import java.util.ArrayList;
import java.util.List;

public class TreeRange extends Tree {

    private List<Node<Range>> nodes = new ArrayList<>();
    private List<Range> leafRanges = new ArrayList<>();

    public TreeRange(String attributeHeader) {
        super(attributeHeader);
    }

    /**
     * Add the children to the parent node. If any of the Nodes already exsist use those, otherwise create new nodes.
     * @param parent
     * @param children
     */
    public void add(Range parent, Range... children) {
        Node<Range> node;

        // First time set the root node
        if (root == null) {
            node = new Node<>(parent);
            root = node;
            nodes.add(node);
        } else {
            node = findNode(parent);
        }

        // Otherwise create a new node and add it to the list of nodes.
        if (node == null) {
            throw new IllegalArgumentException("The parent must already be defined in the taxonomy tree");
        }

        // For all children passed in
        for (Range c : children) {
            Node<Range> child = new Node<>(node, c);
            node.addChild(child);
            nodes.add(child);
            leafRanges.add(c);
        }

        if (children.length != 0) {
            leafRanges.remove(parent);
        }

    }

    /**
     * Look for the node within the current array of recorded nodes.
     * @param range
     * @return
     */
    private Node<Range> findNode(Range range) {
        //System.out.println("TreeRange   findNode(range): " + range);
        for (Node<Range> node : nodes) {
            if (node.getData().equals(range)) {
                return node;
            }
        }
        return null;
    }

    private Range findRange(int value) {
        for (Range range : leafRanges) {
            if (range.contains(value)) {
                return range;
            }
        }
        return null;
    }

    private Range findRange(String range) {
        //System.out.println("TreeRange   findNode(range): " + range);
        if (!Range.isRange(range)) {
            return findRange(Integer.parseInt(range));
        }

        for (Node<Range> node : nodes) {
            if (node.getData().equals(range)) { // TODO this is broken Harry
                return node.getData();
            }
        }
        return null;
    }

    // -- Getters --

    /**
     * Returns the generalised value of an attribute by getting the nodes parent 'suppressionLevel' times.
     * @param value
     * @param generalisationLevel
     * @return
     */
    public String getGeneralised(String value, int generalisationLevel) {
        int num = Integer.parseInt(value);
        Range range = findRange(num);

        if (range == null) {
            throw new IllegalArgumentException("TreeRange: Cannot find range containing " + value + " in the taxonomy tree");
        }

        // If the node doesn't exist, throw an error
        Node node = findNode(range);
        if (node == null) {
            throw new IllegalArgumentException("TreeRange: Cannot find range " + range + " in the taxonomy tree");
        }

        return getGeneralisedNode(node, generalisationLevel).toString();
    }

    public List<Node> getNodes() {
        return new ArrayList<>(nodes);
    }

    public Node getNode(Attribute attribute) {
        NumericAttribute attr = (NumericAttribute) attribute; // TODO unchecked cast
        int value = attr.getValue();
        Node<Range> parent = findNode(findRange(value));
        return new Node<>(parent, new Range(value, value));
    }
}
