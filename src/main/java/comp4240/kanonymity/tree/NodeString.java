package comp4240.kanonymity.tree;

public class NodeString extends Node {

    private String value;

    public NodeString(String value) {
        this(null, value);
    }

    public NodeString(NodeString parent, String value) {
        super(parent);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}