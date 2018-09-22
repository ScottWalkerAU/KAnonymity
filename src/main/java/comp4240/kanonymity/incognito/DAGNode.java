package comp4240.kanonymity.incognito;

import comp4240.kanonymity.Dataset;
import comp4240.kanonymity.kanonymity.FullDomainModel;
import comp4240.kanonymity.kanonymity.GeneralisationModel;
import comp4240.kanonymity.kanonymity.KAnonymity;

import java.util.ArrayList;
import java.util.List;

public class DAGNode {

    private List<DAGNode> children;
    private List<FullDomainLevel> generalisations;

    public DAGNode(List<FullDomainLevel> generalisations) {
        this.children = new ArrayList<>();
        this.generalisations = generalisations;
    }

    public void addChild(DAGNode child) {
        if (!children.contains(child)) {
            children.add(child);
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

    public Double getDivergence(KAnonymity kAnonymity) {
        Dataset dataset = kAnonymity.getDataset();
        for (FullDomainLevel generalisation : generalisations) {
            String header = generalisation.getTree().getAttributeHeader();
            int level = generalisation.getLevel();
            GeneralisationModel model = new FullDomainModel(header, level);
            model.setDataset(dataset);
            model.anonymise();
        }
        dataset.displayModifiedDataset();
        System.out.println(this);

        Double divergence = null;
        if (kAnonymity.isAtDesiredK()) {
            divergence = kAnonymity.attributeDivergence();
        }
        dataset.resetModifiedValues();
        return divergence;
    }

    public List<DAGNode> getChildren() {
        return children;
    }

    public List<FullDomainLevel> getGeneralisations() {
        return generalisations;
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
