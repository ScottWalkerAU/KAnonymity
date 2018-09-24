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

    public boolean generalisationsMatch(DAGNode other) {
        if (generalisations.size() != other.generalisations.size()) {
            return false;
        }

        for (int i = 0; i < generalisations.size(); i++) {
            FullDomainLevel genA = generalisations.get(i);
            FullDomainLevel genB = other.generalisations.get(i);

            if (!genA.equalTo(genB)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets if the node is anonymous. Value is null by default as we are uncertain.
     * If setting to true, all the more generalised nodes must also be true.
     * @param anonymous True if anonymous
     */
    public void setAnonymous(Anonymous anonymous) {
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

    public Double getDivergence(KAnonymity kAnonymity) {
        Dataset dataset = kAnonymity.getDataset();
        for (FullDomainLevel generalisation : generalisations) {
            String header = generalisation.getTree().getAttributeHeader();
            int level = generalisation.getLevel();
            GeneralisationModel model = new FullDomainModel(header, level);
            model.setDataset(dataset);
            model.anonymise();
        }
        //dataset.displayModifiedDataset();
        //System.out.println(this);

        Double divergence = null;
        if (kAnonymity.isAtDesiredK()) {
            divergence = kAnonymity.attributeDivergence();
        }
        dataset.resetModifiedValues();
        return divergence;
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
