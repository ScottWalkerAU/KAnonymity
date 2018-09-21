package comp4240.kanonymity.kanonymity;

import comp4240.kanonymity.Dataset;
import comp4240.kanonymity.Record;
import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.attribute.IdentifierType;
import comp4240.kanonymity.tree.Tree;

import java.util.ArrayList;
import java.util.List;

public class SubtreeModel extends GeneralisationModel {

    private int generalisationLevel;

    public SubtreeModel(int generalisationLevel) {
        this.generalisationLevel = generalisationLevel;
    }

    /**
     * {@inheritDoc}
     */
    public void anonymise() {
        Dataset dataset = getDataset();
        List<String> headers = dataset.getHeaders();

        // Loop through all records
        for (Record r : dataset.getRecords()) {
            // Get the attributes for each record
            List<Attribute> attributes = r.getAttributes();

            // Loop through all attributes
            for (int i = 0; i < dataset.getAttributeSize(); i++) {
                Attribute attribute = attributes.get(i);
                IdentifierType identifierType = attribute.getIdentifierType();
                String header = headers.get(i);

                // Get the generalisation tree for that header if it exists
                Tree generalisationTree = dataset.getGeneralisationTree(header);

                // If either the identifier doesn't equal QID or there is no generalisation tree, skip the attribute
                if (identifierType != IdentifierType.QID || generalisationTree == null) {
                    continue;
                }

                // Calculate the generalised value
                String originalValue = attribute.toString();
                String generalisedValue = generalisationTree.getGeneralised(originalValue, generalisationLevel);

                // Update the modified values
                updateModifiedValue(attribute, generalisedValue);

                // Output the new generalised value
                System.out.printf("%s is generalised to %s when using a generalisation level of %d\n", originalValue, generalisedValue, generalisationLevel);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void anonymise(String header) {
        Dataset dataset = getDataset();

        List<Attribute> attributes = dataset.getAttributes(header);

        // Loop through all attributes
        for (Attribute attribute : attributes) {
            IdentifierType identifierType = attribute.getIdentifierType();

            // Get the generalisation tree for that header if it exists
            Tree generalisationTree = dataset.getGeneralisationTree(header);

            // Calculate the generalised value
            String originalValue = attribute.toString();
            String generalisedValue = generalisationTree.getGeneralised(originalValue, generalisationLevel);

            // Update the modified values
            updateModifiedValue(attribute, generalisedValue);

            // Output the new generalised value
            System.out.printf("%s is generalised to %s when using a generalisation level of %d\n", originalValue, generalisedValue, generalisationLevel);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void anonymise(String header, List<Attribute> identifiedAttributes) {
        // TODO this whole method
        return;
    }
}