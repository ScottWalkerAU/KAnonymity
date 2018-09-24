package comp4240.kanonymity.incognito;

import comp4240.kanonymity.kanonymity.KAnonymity;
import comp4240.kanonymity.tree.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public List<GeneralisationResult> getAnonymisations() {
        List<GeneralisationResult> anonymisations = new ArrayList<>();
        for (DAGNode node : nodes) {
            Double divergence = node.getDivergence(kAnonymity);
            if (divergence != null) {
                anonymisations.add(new GeneralisationResult(node, divergence));
            }
        }
        return anonymisations;
    }

    public int size() {
        return nodes.size();
    }

    public void print() {
        for (DAGNode node : nodes)
            System.out.println(node);
    }

}
