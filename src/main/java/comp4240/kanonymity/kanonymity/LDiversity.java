package comp4240.kanonymity.kanonymity;

import comp4240.kanonymity.Dataset;
import comp4240.kanonymity.Record;

import java.util.*;

public class LDiversity extends KAnonymity {

    private int desiredL;

    public LDiversity(Dataset dataset, int desiredK, int desiredL) {
        super(dataset, desiredK);
        this.desiredL = desiredL;
    }

    @Override
    public boolean meetsConditions() {
        return isKAnonymous(desiredK) && isLDiverse(desiredL);
    }

    public boolean isLDiverse(int desiredL) {
        HashMap<String, List<String>> equivalenceClasses = getEquivalenceClasses();

        // Check each list inside the hashmap has > desiredL
        Iterator<Map.Entry<String, List<String>>> itr = equivalenceClasses.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry value = itr.next();
            List<String> sensitiveList = (List<String>) value.getValue();

            if (sensitiveList.size() < desiredL) {
                return false;
            }
        }

        return true;
    }

    public int getL() {
        HashMap<String, List<String>> equivalenceClasses = getEquivalenceClasses();

        // Check each list inside the hashmap has > desiredL
        Integer maxL = null;
        Iterator<Map.Entry<String, List<String>>> itr = equivalenceClasses.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry value = itr.next();
            List<String> sensitiveList = (List<String>) value.getValue();

            System.out.println("Value: " + value + ", List Size: " + sensitiveList.size());

            if (maxL == null || sensitiveList.size() < maxL) {
                maxL = sensitiveList.size();
            }
        }

        return maxL;
    }


    private HashMap<String, List<String>> getEquivalenceClasses () {
        HashMap<String, List<String>> equivalenceClasses = new HashMap<>();

        // Loop through all records
        for (Record r : dataset.getRecords()) {
            // Get the modified values string
            String qids = r.getModifiedQIDValues();
            String sensitive = r.getModifiedSensitiveValues();
            List<String> sensitiveList = equivalenceClasses.get(qids);

            // If the list doesn't exist, create a new list and add it to the hashMap
            if (sensitiveList == null) {
                sensitiveList = new ArrayList<>();
                equivalenceClasses.put(qids, sensitiveList);
            }

            // Put the sensitive value into the list inside the hashmap
            if (!sensitiveList.contains(sensitive)) {
                sensitiveList.add(sensitive);
            }
        }

        return equivalenceClasses;
    }

    @Override
    public void printStats() {
        System.out.println("Dataset has k: " + getK());
        System.out.println("Dataset has L: " + getL());
    }
}
