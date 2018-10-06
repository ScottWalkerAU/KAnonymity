package comp4240.kanonymity.kanonymity;

import comp4240.kanonymity.Dataset;
import comp4240.kanonymity.Record;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
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

        // Check each list inside the HashMap has > desiredL
        for (Map.Entry<String, List<String>> entry : equivalenceClasses.entrySet()) {
            List<String> sensitiveList = entry.getValue();
            if (sensitiveList.size() < desiredL) {
                return false;
            }
        }

        return true;
    }

    public int getL() {
        HashMap<String, List<String>> equivalenceClasses = getEquivalenceClasses();

        // Check each list inside the HashMap has > desiredL
        Integer maxL = null;
        for (Map.Entry<String, List<String>> entry : equivalenceClasses.entrySet()) {
            List<String> sensitiveList = entry.getValue();

            log.debug("Value: " + entry + ", List Size: " + sensitiveList.size());

            if (maxL == null || sensitiveList.size() < maxL) {
                maxL = sensitiveList.size();
            }
        }

        return maxL;
    }


    private HashMap<String, List<String>> getEquivalenceClasses() {
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

            // Put the sensitive value into the list inside the HashMap
            if (!sensitiveList.contains(sensitive)) {
                sensitiveList.add(sensitive);
            }
        }

        return equivalenceClasses;
    }

    @Override
    public void printStats() {
        log.info("Dataset has k: " + getK() +
                "\nDataset has L: " + getL());
    }
}
