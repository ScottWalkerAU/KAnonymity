package comp4240.kanonymity.incognito;

import comp4240.kanonymity.kanonymity.KAnonymity;
import comp4240.kanonymity.tree.Tree;
import lombok.extern.log4j.Log4j2;

import java.util.*;

/**
 * The outer class to hold the nodes within the DAG
 */
@Log4j2
public class DAG {

    /** The settings to use for generalisation */
    private KAnonymity kAnonymity;

    /** Root node of the dag (No generalisations) */
    private DAGNode root;
    /** Map containing all the nodes for quick access */
    private HashMap<String, DAGNode> hashedNodes;

    /**
     * Constructor to create the DAG
     * @param kAnonymity Settings
     */
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

    /**
     * Create every node in the DAG by generalising one attribute to the next level recursively
     */
    private void initialise() {
        List<DAGNode> nodes = new ArrayList<>();
        nodes.add(root);
        hashedNodes.put(root.toString(), root);

        int index = 0;
        int counter = 0;
        long totalCombinations = kAnonymity.getDataset().getTaxonomyTreeCombinations();

        // While there are nodes we have no checked. The size increases as we find more
        while (index < nodes.size()) {
            counter++;
            if (counter % 10 == 0) {
                System.out.print("\rGenerating all combinations: " + (100 * counter / totalCombinations) + "%" );
            }
            DAGNode current = nodes.get(index);

            // For each of the attributes in the generalisation
            List<FullDomainLevel> generalisations = current.getGeneralisations();
            for (int i = 0; i < generalisations.size(); i++) {
                FullDomainLevel generalisation = generalisations.get(i);
                Tree tree = generalisation.getTree();
                int level = generalisation.getLevel();

                // If the level can be generalised further, do it and add it to the DAG
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

        System.out.println("\rFinished Generating the directed acyclic graph (DAG) node connections! In total generated " + counter + "/" + totalCombinations + " necessary combinations.");
    }

    /**
     * Check if a generalisation level has already been found, and create it if not
     * @param nodes List of all nodes
     * @param temp Temporary DAGNode
     * @return Temp if the node did not exist, or the already existing one
     */
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

    /**
     * Find the best generalisation combination
     * @return Best generalisation combination with its fitness
     */
    public GeneralisationResult getBestGeneralisation() {
        GeneralisationResult best = null;
        List<DAGNode> toCheck = new LinkedList<>(hashedNodes.values());


        int roundCounter = 0;   // This will count all the way up to the totalCombinations
        int nodesChecked = 0;    // This will count every time a node has its fitness analysed
        int meetConditions = 0; // This will count every time a valid generalisation is found
        long totalCombinations = kAnonymity.getDataset().getTaxonomyTreeCombinations();
        while (!toCheck.isEmpty()) {
            roundCounter++;
            System.out.printf("\rSearching for the best generalisation: (%d/%d)\t%.4f%%", roundCounter, totalCombinations, (100.0 * roundCounter / totalCombinations));

            int index = new Random().nextInt(toCheck.size());
            DAGNode node = toCheck.remove(index);
            // If we have already calculated the anonymity of the node, skip over it.
            if (node.getAnonymous() != DAGNode.Anonymous.UNCHECKED) {
                continue;
            }

            // TODO - Here it is checking if its valid
            Double fitness = node.getFitness(kAnonymity);
            nodesChecked++;
            // Not anonymous. Add the children and keep checking
            if (fitness == null) {
                node.setAnonymous(DAGNode.Anonymous.FALSE);
                continue;
            }

            // TODO - Here it is actually valid
            meetConditions++;
            node.setAnonymous(DAGNode.Anonymous.TRUE);

            if (best == null || fitness > best.getFitness()) {
                best = new GeneralisationResult(node, fitness);
            }
        }

        System.out.println("\rFinished Searching for the best generalisation! In total checked " + nodesChecked + " generalisations and only " + meetConditions + "/" + totalCombinations + " met the conditions!\n");
        return best;
    }

    /**
     * @return Number of nodes in the DAG
     */
    public int getSize() {
        return hashedNodes.size();
    }
}
