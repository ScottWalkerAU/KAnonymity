package comp4240.kanonymity.incognito;

import comp4240.kanonymity.Dataset;
import comp4240.kanonymity.kanonymity.FullDomainModel;
import comp4240.kanonymity.kanonymity.GeneralisationModel;
import comp4240.kanonymity.kanonymity.KAnonymity;

import java.util.ArrayList;
import java.util.List;

public class DAGNode {

    public enum Anonymous {
        UNCHECKED, TRUE, FALSE
    }

    private List<DAGNode> parents;
    private List<DAGNode> children;
    private List<FullDomainLevel> generalisations;
    private Anonymous anonymous;

    public DAGNode(List<FullDomainLevel> generalisations) {
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.generalisations = generalisations;
        this.anonymous = Anonymous.UNCHECKED;
    }

    public void addChild(DAGNode child) {
        if (!children.contains(child)) {
            children.add(child);
        }
        if (!child.parents.contains(this)) {
            child.parents.add(this);
        }
    }

    /**
     * Sets if the node is anonymous. Value is null by default as we are uncertain.
     * If setting to true, all the more generalised nodes must also be true.
     * @param anonymous True if anonymous
     */
    public void setAnonymous(Anonymous anonymous) {
        if (this.anonymous != Anonymous.UNCHECKED) {
            return;
        }

        this.anonymous = anonymous;
        switch (anonymous) {
            case FALSE:
                // If this level is false, all the parents must be false too
                for (DAGNode parent : parents) {
                    parent.setAnonymous(anonymous);
                }
                break;
            case TRUE:
                // If this level is true, all more generalised levels must be true too
                for (DAGNode child : children) {
                    child.setAnonymous(anonymous);
                }
                break;
        }
    }

    public void anonymise(KAnonymity kAnonymity) {
        Dataset dataset = kAnonymity.getDataset();
        for (FullDomainLevel generalisation : generalisations) {
            String header = generalisation.getTree().getAttributeHeader();
            int level = generalisation.getLevel();
            GeneralisationModel model = new FullDomainModel(header, level);
            model.setDataset(dataset);
            model.anonymise();
        }
    }

    public Double getFitness(KAnonymity kAnonymity) {
        anonymise(kAnonymity);

        Double fitness = null;
        if (kAnonymity.meetsConditions()) {
            fitness = kAnonymity.getFitness();
        }
        kAnonymity.getDataset().resetModifiedValues();
        return fitness;
    }

    public List<FullDomainLevel> getGeneralisations() {
        return generalisations;
    }

    public Anonymous getAnonymous() {
        return anonymous;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("< ");
        for (int i = 0; i < generalisations.size(); i++) {
            if (i > 0)
                builder.append(", ");
            builder.append(generalisations.get(i));
        }
        builder.append(" >");
        return builder.toString();
    }
}
