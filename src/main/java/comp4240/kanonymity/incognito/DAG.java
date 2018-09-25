package comp4240.kanonymity.incognito;

import comp4240.kanonymity.kanonymity.KAnonymity;
import comp4240.kanonymity.tree.Tree;

import java.util.*;

public class DAG {

    private KAnonymity kAnonymity;

    private DAGNode root;
    private HashMap<String, DAGNode> hashedNodes;

    public DAG(KAnonymity kAnonymity) {
        this.kAnonymity = kAnonymity;
        this.hashedNodes = new HashMap<>();

        // Create the root node of which all other nodes will be created from
        List<Tree> trees = kAnonymity.getDataset().getGeneralisations();
        List<FullDomainLevel> generalisations = new ArrayList<>(trees.size());
        for (Tree tree : trees) {
            int level = tree.getTreeHeight();
            generalisations.add(new FullDomainLevel(tree, level));
        }
        root = new DAGNode(generalisations);
        initialise();
    }

    private void initialise() {
        List<DAGNode> nodes = new ArrayList<>();
        nodes.add(root);
        hashedNodes.put(root.toString(), root);

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
                    DAGNode next = findNode(nodes, new DAGNode(nextGens));
                    current.addChild(next);
                }
            }

            index++;
        }
    }

    private DAGNode findNode(List<DAGNode> nodes, DAGNode temp) {
        DAGNode node = hashedNodes.get(temp.toString());
        if (node != null) {
            return node;
        }
        // Not in the DAG yet, add it and return it
        nodes.add(temp);
        hashedNodes.put(temp.toString(), temp);
        return temp;
    }

    public GeneralisationResult getBestGeneralisation() {
        GeneralisationResult best = null;
        List<DAGNode> toCheck = new LinkedList<>(hashedNodes.values());

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
        return hashedNodes.size();
    }
}
