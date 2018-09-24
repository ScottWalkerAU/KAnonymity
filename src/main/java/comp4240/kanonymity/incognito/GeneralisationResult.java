package comp4240.kanonymity.incognito;


public class GeneralisationResult {

    private DAGNode node;
    private double divergence;

    public GeneralisationResult(DAGNode node, double divergence) {
        this.node = node;
        this.divergence = divergence;
    }

    public DAGNode getNode() {
        return node;
    }

    public double getDivergence() {
        return divergence;
    }

    @Override
    public String toString() {
        return String.format("DAGNode: %s, Divergence: %f", node, divergence);
    }
}
