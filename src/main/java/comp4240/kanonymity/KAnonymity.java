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
        this.k = k;
    }

    public void anonymise() {
        // Get the headers from the dataset
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
                String generalisedValue = generalisationTree.getGeneralised(originalValue, suppressionLevel);

                // Output the new generalised value
                System.out.printf("%s is generalised to %s when using a suppression level of %d\n", originalValue, generalisedValue, suppressionLevel);
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
        for (Record r1 : records) {
            // Count the number of matches for this record
            int matches = 0;
            for (Record r2 : records) {
                if (r1.equivalentTo(r2)) {
                    matches++;
                }
            }

            // If we've found a worse value than what we're looking for, return false
            if (matches < k) {
                return false;
            }
            // If we've found a worse k value, update it
            else if (k < minK) {
                minK = k;
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
        for (Record r1 : records) {
            // Count the number of matches for this record
            int matches = 0;
            for (Record r2 : records) {
                if (r1.equivalentTo(r2)) {
                    matches++;
                }
            }

            // Can't get any worse than 1, not reason to keep looking
            if (matches == 1) {
                return 1;
            }
            // If we've found a worse k value, update it
            else if (matches < minK) {
                minK = matches;
            }
        }
        return minK;
    }

    public void AttributeDivergence() {
        System.out.println("\nAttribute Divergence");
        List<String> headers = dataset.getHeaders();
        List<Record> records = dataset.getRecords();

        // Loop through all records one Attribute column at a time
        for (int i = 0; i < headers.size(); i++) {
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
            System.out.println(headers.get(i) + ": Unique values: " + values.size() + ", percentage of data is " + (100 * records.size() / values.size() / records.size()) + "%");
        }
    }
}
