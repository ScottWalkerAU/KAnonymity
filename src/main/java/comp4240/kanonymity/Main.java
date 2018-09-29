package comp4240.kanonymity;

import comp4240.kanonymity.kanonymity.KAnonymity;
import comp4240.kanonymity.kanonymity.LDiversity;
import comp4240.kanonymity.tree.Range;
import comp4240.kanonymity.tree.TreeDefault;
import comp4240.kanonymity.tree.TreeRange;

public class Main {

    private String fileName;

    public static void main(String[] args) {
        new Main("data/CensusDataRandomSalary100.csv", "data/CensusDataTaxonomy.txt");
    }

    private Main(String... files) {
        long startTime = System.currentTimeMillis();
        this.fileName = files[0];
        Dataset dataset;

        if (files.length > 1) {
            dataset = new Dataset(fileName, files[1]);
        } else {
            dataset = new Dataset(fileName);
        }

        System.out.println("The number of combinations for the loaded taxonomy trees is " + dataset.getTaxonomyTreeCombinations() + " combinations");

        //dataset.displayDataset();
        dataset.displayDataset(10);

        // Create the K-Anonymity class
        //KAnonymity kAnonymity = new KAnonymity(dataset, 2);

        // Create the L-Diverse class
        KAnonymity lDiversity = new LDiversity(dataset, 1, 1);

        // Anonymise the data set
        lDiversity.anonymise();
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed Time: " + (elapsedTime / 1000.0) + " seconds.");

        dataset.getEquivalenceClass();

        lDiversity.printStats();


    }

}