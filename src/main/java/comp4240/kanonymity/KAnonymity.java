package comp4240.kanonymity;

import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.attribute.IdentifierType;
import comp4240.kanonymity.tree.Tree;

import java.util.ArrayList;
import java.util.List;

public class KAnonymity {

    private int k;
    private int suppressionLevel;
    private Dataset dataset;

    KAnonymity(Dataset dataset, int k) {
        this.dataset = dataset;
        this.k = 2;
    }

    public void anonymise() {
        // Get the headers from the dataset
        String[] headers = dataset.getHeaders();

        // Loop through all records
        for (Record r : dataset.getRecords()) {
            // Get the attributes for each record
            List<Attribute> attributes = r.getAttributes();

            // Loop through all attributes
            for (int i = 0; i < dataset.getAttributeSize(); i++) {
                // Get the current attribute
                Attribute attribute = attributes.get(i);

                // Get the identifier type (ID, QID, SENSITIVE)
                IdentifierType identifierType = attribute.getIdentifierType();

                // Get the current header name for the attribute
                String header = headers[i];

                // Get the generalisation tree for that header if it exists
                Tree generalisationTree = dataset.getGeneralisationTree(header);

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

    /**
     * Giving a size k, the method will return whether of not the dataset is k-anonymised
     * @param k the size of k
     * @return true of false if the dataset is k-anonymised
     */
    public boolean isKAnonymous(int k) {
        List<Record> records = dataset.getRecords();
        int minK = records.size();

        // For each record check if there are at least k matches
        for (int i = 0; i < records.size(); i++) {
            int matches = 0;
            Record r1 = records.get(i);

            // Check all other records for a match
            for (int j = 0; j < records.size(); j++) {
                Record r2 = records.get(j);

                if (r1.equivalentTo(r2)) {
                    matches++;
                }
            }

            if (k < minK) {
                minK = k;
            }

            if (matches < k) {
                return false;
            }
        }

        return true;
    }

    /**
     * Calculates the size of k from the current dataset.
     * @return the size of k.
     */
    public int getK() {
        List<Record> records = dataset.getRecords();
        int minK = records.size();

        // For each record check if there are at least k matches
        for (int i = 0; i < records.size(); i++) {
            int matches = 0;
            Record r1 = records.get(i);

            // Check all other records for a match
            for (int j = 0; j < records.size(); j++) {
                Record r2 = records.get(j);

                if (r1.equivalentTo(r2)) {
                    matches++;
                }
            }

            // Can't get any worse than 1, not reason to keep looking
            if (matches == 1) {
                return 1;
            }

            if (matches < minK) {
                minK = matches;
            }
        }

        return minK;
    }

    public void AttributeDivergence() {
        System.out.println("\nAttribute Divergence");
        String[] headers = dataset.getHeaders();
        List<Record> records = dataset.getRecords();

        // Loop through all records one Attribute column at a time
        for (int i = 0; i < headers.length; i++) {
            List<String> values = new ArrayList<>();

            // Record all the different variable types
            for (Record r : records) {
                List<Attribute> recordAttributes = r.getAttributes();
                Attribute a = recordAttributes.get(i);
                String value = a.toString();

                if (!values.contains(value)) {
                    values.add(value);
                }
            }
            // Calculate how many different results are found
            System.out.println(headers[i] + ": Unique values: " + values.size() + ", percentage of data is " + (100 * records.size() / values.size() / records.size()) + "%");
        }
    }
}
