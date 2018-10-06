package comp4240.kanonymity.incognito;

import comp4240.kanonymity.tree.Tree;

public class FullDomainLevel {

    /** The tree this generalisation is for */
    private Tree tree;
    /** What level to generalise to */
    private int level;

    public FullDomainLevel(Tree tree, int level) {
        this.tree = tree;
        this.level = level;
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
