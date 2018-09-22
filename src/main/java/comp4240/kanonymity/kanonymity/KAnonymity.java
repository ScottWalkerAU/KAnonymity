package comp4240.kanonymity.kanonymity;

import comp4240.kanonymity.Dataset;
import comp4240.kanonymity.Record;
import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.attribute.AttributeType;
import comp4240.kanonymity.attribute.IdentifierType;
import comp4240.kanonymity.attribute.NumericAttribute;
import comp4240.kanonymity.tree.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class KAnonymity {

    private int k;
    private Dataset dataset;

    public KAnonymity(Dataset dataset, int k) {
        this.dataset = dataset;
        this.k = k;
    }

    public void anonymise() {
        // TODO This will be the method to find the best combination of generalisations for k
    }

    // TODO temporary method, remove later.
    public void anonymiseTest() {
        Node parent = dataset.getGeneralisationTree("Age").getRoot();
        Node child = parent.getChildren().get(0);
        GeneralisationModel model = new SiblingModel("Age", parent, child);
        model.setDataset(dataset);
        model.anonymise();
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

    public double attributeDivergence() {
        List<String> headers = dataset.getHeaders();
        List<IdentifierType> types = dataset.getIdentifiers();

        double total = 0.0;
        int count = 0;

        // Loop through all records one Attribute column at a time
        for (int i = 0; i < headers.size(); i++) {
            IdentifierType type = types.get(i);
            if (type != IdentifierType.QID) {
                continue;
            }

            String header = headers.get(i);
            List<String> values = new ArrayList<>();

            // Record all the different variable types
            for (Attribute a : dataset.getAttributes(header)) {
                String value = a.toString();
                if (!values.contains(value)) {
                    values.add(value);
                }
            }
            // Calculate the percentage how many different results are found
            total += 100.0 / values.size();
            count++;
        }
        // Average of the total divergence
        return total / count;
    }
}
