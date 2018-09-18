package comp4240.kanonymity.tree;

import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.List;

public class TreeRange extends Tree {

    private List<NodeRange> nodes = new ArrayList<>();
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
        NodeRange node;

        // First time set the root node
        if (root == null) {
            node = new NodeRange(parent);
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
            NodeRange child = new NodeRange(node, c);
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
    private NodeRange findNode(Range range) {
        for (NodeRange node : nodes) {
            if (node.getRange() == range) {
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

    /**
     * {@Inheritdoc}
     */
    @Override
    public List<String> getSubtree(String value) {
        List<String> values = new ArrayList<>();
        List<Node> queue = new ArrayList<>();

        int number = Double.valueOf(value).intValue();
        Range range = findRange(number);
        if (range == null) {
            throw new IllegalArgumentException("Cannot find range containing " + value + " in the taxonomy tree");
        }
        // Add the current value to the queue
        queue.add(findNode(range));

        while (!queue.isEmpty()) {
            Node n = queue.remove(0);

            values.add(n.getValue());

            for (Node c : n.getChildren()) {
                queue.add(c);
            }
        }

        return values;
    }

    // -- Getters --

    /**
     * Returns the generalised value of an attribute by getting the nodes parent 'suppressionLevel' times.
     * @param value
     * @param generalisationLevel
     * @return
     */
    public String getGeneralised(String value, int generalisationLevel) {
        int number = Double.valueOf(value).intValue();
        Range range = findRange(number);
        if (range == null) {
            throw new IllegalArgumentException("Cannot find range containing " + value + " in the taxonomy tree");
        }

        // If the node doesn't exist, throw an error
        Node node = findNode(range);
        if (node == null) {
            throw new IllegalArgumentException("Cannot find range " + range + " in the taxonomy tree");
        }

        return getGeneralisedNode(node, generalisationLevel).getValue();
    }
}
