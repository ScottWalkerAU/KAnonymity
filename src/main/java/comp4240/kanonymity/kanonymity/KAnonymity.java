package comp4240.kanonymity.kanonymity;

import comp4240.kanonymity.Dataset;
import comp4240.kanonymity.Record;
import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.attribute.IdentifierType;
import comp4240.kanonymity.incognito.DAG;
import comp4240.kanonymity.incognito.GeneralisationResult;
import comp4240.kanonymity.tree.Node;

import java.util.*;


public class KAnonymity {

    protected int desiredK;
    protected Dataset dataset;

    public KAnonymity(Dataset dataset, int desiredK) {
        this.dataset = dataset;
        this.desiredK = desiredK;
    }

    public void anonymise() {
        DAG dag = new DAG(this);
        GeneralisationResult best = dag.getBestGeneralisation();
        System.out.println("Best generalisation combo: " + best);

        if (best != null) {
            best.getNode().anonymise(this);
        }
        dataset.displayModifiedDataset(10);
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
        // Returns true if k is equal to 1. All data sets have a k of 1.
        if (k == 1) {
            return true;
        }

        // Create a queue of all records to iterate through.
        List<Record> recordsQueue = new LinkedList<>(dataset.getRecords());

        // For each record check if there are at least k matches
        while (!recordsQueue.isEmpty()) {
            // Get the number of matched records
            int matches = findMatches(recordsQueue);

            // If matches is less than k return false.
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
        // Create a queue of all records to iterate through.
        List<Record> recordsQueue = new LinkedList<>(dataset.getRecords());

        // Finds the lowest k value within the data set.
        int maxK = recordsQueue.size();

        // For each record check if there are at least k matches
        while (!recordsQueue.isEmpty()) {
            // Get the number of matched records
            int matches = findMatches(recordsQueue);
            
            // If matches == 1, you can't get a worse value of 1. So just return 1.
            if (matches == 1) {
                return 1;
            } else if (matches < maxK) { // If we've found a worse k value, update it
                maxK = matches;
            }
        }

        return maxK;
    }

    /**
     * Given an List of records, return the number of times the first element if found within that list.
     *
     * @param recordsQueue  List of Records
     * @return              The number of matches the first element is found within that list.
     */
    private int findMatches(List<Record> recordsQueue) {
        // Get the first element of the list
        Record r1 = recordsQueue.remove(0);

        // Count the number of matches for this record
        int matches = 1;

        // Uses an iterator to look for matching pair
        Iterator<Record> itr = recordsQueue.iterator();
        while(itr.hasNext()) {
            Record r2 = itr.next();

            // When found removes the Record from the list to speed things up.
            if (r1.equivalentTo(r2)) {
                matches++;
                itr.remove();
            }
        }

        return matches;
    }


    /**
     * For KAnonymity it, just ensures that k is met.
     *
     * @return
     */
    public boolean meetsConditions() {
        return isKAnonymous(desiredK);
    }

    public double getFitness() {
        HashMap<String, Integer> equivalentClasses = new HashMap<>();

        for (Record r : dataset.getRecords()) {
            String contents = r.getModifiedQIDValues();
            Integer count = equivalentClasses.get(contents);
            count = (count == null) ? 1 : count + 1;
            equivalentClasses.put(contents, count);
        }

        return equivalentClasses.size();
    }

    public int getDesiredK() {
        return desiredK;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void printStats() {
        System.out.println("Dataset has k: " + getK());
    }
}
