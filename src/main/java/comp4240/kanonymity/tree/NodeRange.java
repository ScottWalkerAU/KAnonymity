package comp4240.kanonymity.tree;


public class NodeRange extends Node {

    private Range range;

    public NodeRange(Range range) {
        this(null, range);
    }

    public NodeRange(NodeRange parent, Range range) {
        super(parent);
        this.range = range;
    }

    public String getValue() {
        return range.toString();
    }

    public Range getRange() {
        return range;
    }
}