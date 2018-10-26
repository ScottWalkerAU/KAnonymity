package comp4240.kanonymity.incognito;

import comp4240.kanonymity.kanonymity.KAnonymity;
import comp4240.kanonymity.tree.Tree;
import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
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
        int counter = 0;
        int totalCombinations = kAnonymity.getDataset().getTaxonomyTreeCombinations();
        while (index < nodes.size()) {
            counter++;
            if (counter % 10 == 0) {
                System.out.print("\rGenerating all combinations: " + (100 * counter / totalCombinations) + "%" );
            }
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

        System.out.println("\rFinished Generating all combinations!");
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

        int counter = 0;
        int totalCombinations = kAnonymity.getDataset().getTaxonomyTreeCombinations();
        while (!toCheck.isEmpty()) {
            counter++;
            //System.out.print("\rSearching for the best generalisation: (" + counter + "/" + totalCombinations + ")\t" + (100.0 * counter / totalCombinations ) + "%" );
            System.out.printf("\rSearching for the best generalisation: (%d/%d)\t%.4f%%", counter, totalCombinations, (100.0 * counter / totalCombinations));
            int index = new Random().nextInt(toCheck.size());
            DAGNode node = toCheck.remove(index);
            // If we have already calculated the anonymity of the node, skip over it.
            if (node.getAnonymous() != DAGNode.Anonymous.UNCHECKED) {
                continue;
            }

            //Double fitness = node.getFitness(kAnonymity);
            Double fitness = node.getFitness(kAnonymity);
            // Not anonymous. Add the children and keep checking
            if (fitness == null) {
                node.setAnonymous(DAGNode.Anonymous.FALSE);
                continue;
            }

            node.setAnonymous(DAGNode.Anonymous.TRUE);
            //log.debug(node + ": Fitness: " + fitness);

            if (best == null || fitness > best.getFitness()) {
                best = new GeneralisationResult(node, fitness);
            }
        }

        System.out.println("\nFinished Searching for the best generalisation!\n");
        return best;
    }

    public int getSize() {
        return hashedNodes.size();
    }
}
