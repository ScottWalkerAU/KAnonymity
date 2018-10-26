package comp4240.kanonymity;

import comp4240.kanonymity.incognito.DAGNode;
import comp4240.kanonymity.incognito.FullDomainLevel;
import comp4240.kanonymity.tree.Tree;

import java.util.HashMap;
import java.util.List;

public class Statistics {

    public static void getDatasetUtility(DAGNode node) {
        List<FullDomainLevel> generalisations = node.getGeneralisations();

        double utilitySum = 0;
        System.out.println("\nUtility for individual attributes are:");
        for (FullDomainLevel generalisation : generalisations) {
            double utility = getTaxonomyTreeUtility(generalisation);
            utilitySum += utility;

            String attributeHeader = generalisation.getTree().getAttributeHeader();
            System.out.println("    " + attributeHeader + ": " + (int)(utility * 100) + "%");
        }

        utilitySum /= generalisations.size();
        System.out.println("The total utiltiy of the dataset is " + (int)(utilitySum * 100) + "%\n");

    }

    public static double getTaxonomyTreeUtility(FullDomainLevel generalisation) {
        Tree tree = generalisation.getTree();
        int treeHeight = tree.getTreeHeight();
        int level = generalisation.getLevel();
        double utility = 1.0 * level / treeHeight;

        return utility;
    }

    public static void getEquiverlenceClassEntropy(Dataset dataset) {
        HashMap<String, List<Record>> equivalenceClasses =  dataset.getEquivalenceClassesRecords();

        for (String key : equivalenceClasses.keySet()) {
            List<Record> equivalenceClass = equivalenceClasses.get(key);
            HashMap<String, Integer> sensitiveValues = new HashMap<>();

            // Calculate the total number of occurances of each sensitive value
            for (Record r : equivalenceClass) {
                String sensitiveValue = r.getModifiedSensitiveValues();

                Integer occurances = sensitiveValues.get(sensitiveValue);

                occurances = (occurances == null) ? 1 : occurances + 1;

                sensitiveValues.put(sensitiveValue, occurances);

                /*
                if (equivalenceClass.size() < 10) {
                    System.out.println(sensitiveValue + ", occurances so far = " + occurances);
                }
                */
            }

            double entropy = 0;

            for (Integer occurrence : sensitiveValues.values()) {
                double percent = 1.0 * occurrence / equivalenceClass.size();
                entropy += percent * (Math.log(percent) / Math.log(2));
            }

            if (entropy != 0) {
                entropy = -entropy;
            }
            entropy = (Math.floor(entropy * 100)) / 100.0;
            //System.out.println(key + "\nClass size = " + equivalenceClass.size() + ", Entropy = " + entropy + "\n");

            // TODO - Used to get the data out for graphing
            System.out.println(entropy);
        }
    }
}