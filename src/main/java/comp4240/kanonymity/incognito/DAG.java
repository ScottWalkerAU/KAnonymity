package comp4240.kanonymity.incognito;

import comp4240.kanonymity.kanonymity.KAnonymity;
import comp4240.kanonymity.tree.Tree;

import java.util.*;

public class DAG {

    private KAnonymity kAnonymity;
    private List<Tree> trees;

    private DAGNode root;
    private List<DAGNode> nodes;

    public DAG(KAnonymity kAnonymity) {
        this.kAnonymity = kAnonymity;
        this.trees = kAnonymity.getDataset().getGeneralisations();

        nodes = new ArrayList<>();
        List<FullDomainLevel> generalisations = new ArrayList<>(trees.size());
        for (Tree tree : trees) {
            int level = tree.getTreeHeight();
            generalisations.add(new FullDomainLevel(tree, level));
        }
        root = new DAGNode(generalisations);
        nodes.add(root);
        initialise();
    }

    private void initialise() {
        int index = 0;
        while (index < nodes.size()) {
            DAGNode current = nodes.get(index);

            List<FullDomainLevel> generalisations = current.getGeneralisations();
            for (int i = 0; i < generalisations.size(); i++) {
                FullDomainLevel generalisation = generalisations.get(i);
                Tree tree = generalisation.getTree();
                int level = generalisation.getLevel();

                if (level > 0) {
                    List<FullDomainLevel> nextGens = new ArrayList<>(generalisations);
                    FullDomainLevel nextGen = new FullDomainLevel(tree, level - 1);
                    nextGens.set(i, nextGen);
                    DAGNode next = findNode(new DAGNode(nextGens));
                    current.addChild(next);
                }
            }

            index++;
        }
    }

    private DAGNode findNode(DAGNode temp) {
        for (DAGNode node : nodes) {
            // Check existing nodes
            if (node.generalisationsMatch(temp)) {
                return node;
            }
        }
        // Not in the DAG yet, add it and return it
        nodes.add(temp);
        return temp;
    }

    public GeneralisationResult getBestGeneralisation() {
        GeneralisationResult best = null;
        List<DAGNode> toCheck = new LinkedList<>(nodes);

        while (!toCheck.isEmpty()) {
            int index = new Random().nextInt(toCheck.size());
            DAGNode node = toCheck.remove(index);
            // If we have already calculated the anonymity of the node, skip over it.
            if (node.getAnonymous() != DAGNode.Anonymous.UNCHECKED) {
                continue;
            }

            Double divergence = node.getDivergence(kAnonymity);
            // Not anonymous. Add the children and keep checking
            if (divergence == null) {
                node.setAnonymous(DAGNode.Anonymous.FALSE);
                continue;
            }

            node.setAnonymous(DAGNode.Anonymous.TRUE);
            if (best == null || divergence < best.getDivergence()) {
                best = new GeneralisationResult(node, divergence);
            }
        }

        return best;
    }

    public int size() {
        return nodes.size();
    }

    public void print() {
        for (DAGNode node : nodes)
            System.out.println(node);
    }

}
