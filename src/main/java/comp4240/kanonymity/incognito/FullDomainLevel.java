package comp4240.kanonymity.incognito;

import comp4240.kanonymity.tree.Tree;

public class FullDomainLevel {

    private Tree tree;
    private int level;

    public FullDomainLevel(Tree tree, int level) {
        this.tree = tree;
        this.level = level;
    }

    public boolean equalTo(FullDomainLevel other) {
        return (other != null && getTree() == other.getTree() && getLevel() == other.getLevel());
    }

    public Tree getTree() {
        return tree;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return String.format("%s%d", tree.getAttributeHeader(), level);
    }
}
