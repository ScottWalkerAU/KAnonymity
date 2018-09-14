package comp4240.kanonymity;

import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.attribute.IdentifierType;
import comp4240.kanonymity.tree.Tree;

import java.util.List;

public class KAnonymity {

    private int k;
    private int suppressionLevel;

    KAnonymity(int k) {
        this.k = 2;
    }

    public void anonymise(Dataset data) {
        // Get the headers from the dataset
        String[] headers = data.getHeaders();

        // Loop through all records
        for (Record r : data.getRecords()) {
            // Get the attributes for each record
            List<Attribute> attributes = r.getAttributes();

            // Loop through all attributes
            for (int i = 0; i < data.getAttributeSize(); i++) {
                // Get the current attribute
                Attribute attribute = attributes.get(i);

                // Get the identifier type (ID, QID, SENSITIVE)
                IdentifierType identifierType = attribute.getIdentifierType();

                // Get the current header name for the attribute
                String header = headers[i];

                // Get the generalisation tree for that header if it exists
                Tree generalisationTree = data.getGeneralisationTree(header);

                // If either the identifier doesn't equal QID or there is no generalisation tree, skip the attribute
                if (identifierType != IdentifierType.QID || generalisationTree == null) {
                    continue;
                }

                // Otherwise get the attributes value
                String value = attribute.toString();

                // Generalise the value to the certain suppression level
                String generalisedValue = generalisationTree.getGeneralised(value, suppressionLevel);

                // Output the new generalised value
                System.out.println(value + " is generalised to " + generalisedValue + " when using a suppression level of " + suppressionLevel);
            }
        }
    }

    public void setSuppressionLevel(int suppressionLevel) {
        this.suppressionLevel = suppressionLevel;
    }
}
