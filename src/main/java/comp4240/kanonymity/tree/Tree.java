package comp4240.kanonymity.tree;

public abstract class Tree {
    protected Node root = null;

    // ----- Getters -----

    public Node getRoot() {
        return root;
    }

    public int getTreeHeight() {
        return root.getHeight();
    }

    // ----- Setters -----
    public void setRoot(Node root) {
        this.root = root;
    }
}
