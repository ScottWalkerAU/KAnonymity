package comp4240.kanonymity.incognito;

/**
 * A simple container to store a node and its fitness result
 */
public class GeneralisationResult {

    /** Node that was used */
    private DAGNode node;
    /** Its fitness */
    private double fitness;

    /**
     * Constructor
     * @param node DAGNode
     * @param fitness Fitness
     */
    public GeneralisationResult(DAGNode node, double fitness) {
        this.node = node;
        this.fitness = fitness;
    }

    // -- Getters --

    public DAGNode getNode() {
        return node;
    }

    public double getFitness() {
        return fitness;
    }

    // -- Overrides --

    @Override
    public String toString() {
        return String.format("DAGNode: %s, Fitness: %f", node, fitness);
    }
}
