package comp4240.kanonymity.kanonymity;

import comp4240.kanonymity.Dataset;
import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.tree.Tree;

import java.util.List;

public class FullDomainModel extends GeneralisationModel {

    private int generalisationLevel;

    public FullDomainModel(String header, int generalisationLevel) {
        super(header);
        this.generalisationLevel = generalisationLevel;
    }


    /**
     * {@inheritDoc}
     */
    public void anonymise() {
        Dataset dataset = getDataset();
        List<Attribute> attributes = dataset.getAttributes(getHeader());
        Tree generalisationTree = dataset.getGeneralisationTree(getHeader());

        // No tree? Just return without doing anything
        if (generalisationTree == null) {
            return;
        }

        // Loop through all attributes
        for (Attribute attribute : attributes) {
            // Calculate the generalised value
            String originalValue = attribute.toString();
            String generalisedValue = generalisationTree.getGeneralised(originalValue, generalisationLevel);

            // Update the modified values
            updateModifiedValue(attribute, generalisedValue);
        }
    }
}
