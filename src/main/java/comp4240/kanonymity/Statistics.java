package comp4240.kanonymity;

import comp4240.kanonymity.incognito.DAGNode;
import comp4240.kanonymity.incognito.FullDomainLevel;
import comp4240.kanonymity.tree.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class Statistics {

    public static double getDatasetUtility(DAGNode node) {
        List<FullDomainLevel> generalisations = node.getGeneralisations();

        StringBuilder builder = new StringBuilder();

        double utilitySum = 0;
        System.out.println("\nUtility for individual attributes are:");
        for (FullDomainLevel generalisation : generalisations) {
            double utility = getTaxonomyTreeUtility(generalisation);
            utilitySum += utility;

            String attributeHeader = generalisation.getTree().getAttributeHeader();
            System.out.println("    " + attributeHeader + ": " + (int)(utility * 100) + "%");
            builder.append(attributeHeader + ", " + (int)(utility * 100) + "\n");
        }

        utilitySum /= generalisations.size();
        System.out.println("The total utility of the data set is " + (int)(utilitySum * 100) + "%");
//        builder.append("Total," + (int)(utilitySum * 100) + "\n");

        try {
            PrintWriter pw = new PrintWriter(new File("statistics.csv"));
            pw.write(builder.toString());
            pw.close();
            System.out.println("(The utility for each attribute in the data set has been output successful to the file 'statisics.csv')\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return utilitySum;
    }

    public static double getTaxonomyTreeUtility(FullDomainLevel generalisation) {
        Tree tree = generalisation.getTree();
        int treeHeight = tree.getTreeHeight();
        int level = generalisation.getLevel();
        double utility = 1.0 * level / treeHeight;

        return utility;
    }

    public static void getMinimumEquiverlenceClassEntropy(Dataset dataset) {
        HashMap<String, List<Record>> equivalenceClasses =  dataset.getEquivalenceClassesRecords();
        Double minEntropy = null;
        Double maxEntropy = null;
        Double avgEntropy = 0.0;

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

            // Calculate the entropy using the number of occurrences of each value and the total number of occurances.
            for (Integer occurrence : sensitiveValues.values()) {
                double percent = 1.0 * occurrence / equivalenceClass.size();
                entropy += percent * (Math.log(percent) / Math.log(2));
            }

            // The entropy calculation is a negative sum.
            if (entropy != 0) {
                entropy = -entropy;
            }

            // Round to 2dp
            entropy = (Math.floor(entropy * 100)) / 100.0;

            // add to the average entropy
            avgEntropy += entropy;

            // Set the min entropy found.
            if (minEntropy == null || entropy < minEntropy) {
                minEntropy = entropy;
            }

            // Set the max entropy found.
            if (maxEntropy == null || entropy > maxEntropy) {
                maxEntropy = entropy;
            }


            //System.out.println(key + "\nClass size = " + equivalenceClass.size() + ", Entropy = " + entropy + "\n");

            // TODO - Used to get the data out for graphing
            //System.out.println(entropy);
        }

        // Calculate average entropy
        avgEntropy /= equivalenceClasses.size();

        // Output the stats
        System.out.println("Minimum Entropy for the data set is: " + minEntropy);
        System.out.println("Maximum Entropy for the data set is: " + maxEntropy);
        System.out.printf("Average Entropy for the data set is: %.2f\n\n", avgEntropy);
    }

    String toCSV() {
        StringBuilder builder = new StringBuilder();


        return builder.toString();
    }
}