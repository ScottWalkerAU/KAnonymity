package comp4240.kanonymity.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Node within a tree
 * @param <T> The data type stored within
 */
public class Node<T> {

    private T data;

    private Node<T> parent;
    private List<Node<T>> children;
    private int level;

    public Node(T data) {
        this(null, data);
    }

    public Node(Node<T> parent, T data) {
        this.data = data;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.level = parent == null ? 0 : parent.getLevel() + 1;
    }

    /**
     * Add one or more children to the node.
     * @param node Child to be added
     */
    public void addChild(Node<T> node) {
        children.add(node);
    }

    // -- Getters --

    public T getData() {
        return data;
    }

    public Node<T> getParent() {
        return parent;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public int getLevel() {
        return level;
    }

    /**
     * @return Return the height of the tree from the root the the furthest node.
     */
    public int getHeight() {
        int height = 0;

        for (Node c : children) {
            height = Math.max(height, 1 + c.getHeight());
        }

        return height;
    }

    /**
     * @param node Higher node
     * @return If this node can generalise to the other node
     */
    public boolean canGeneraliseTo(Node<T> node) {
        Node<T> parent = this;
        while (parent != null) {
            if (parent == node)
                return true;

            parent = parent.getParent();
        }
        return false;
    }

    // -- Overrides --

    @Override
    public String toString() {
        return data.toString();
    }
}