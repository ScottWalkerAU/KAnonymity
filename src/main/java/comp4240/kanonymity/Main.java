package comp4240.kanonymity;

import comp4240.kanonymity.kanonymity.KAnonymity;
import comp4240.kanonymity.tree.Range;
import comp4240.kanonymity.tree.TreeDefault;
import comp4240.kanonymity.tree.TreeRange;

public class Main {

    private String fileName;

    public static void main(String[] args) {
        new Main("CensusData10.csv", "CensusDataTaxonomy.txt");
    }

    private Main(String... files) {
        this.fileName = files[0];
        Dataset dataset;

        if (files.length > 1) {
            dataset = new Dataset(fileName, files[1]);
        } else {
            dataset = new Dataset(fileName);
        }

        System.out.println("The number of combinations for the loaded taxonomy trees is " + dataset.getTaxonomyTreeCombinations() + " combinations");

        //dataset.displayDataset();
        dataset.displaySomeDataset();

        // Create the K-Anonymity class
        KAnonymity kAnonymity = new KAnonymity(dataset, 2);

        // Print the results
        /*System.out.println("\nGeneralised values:");
        kAnonymity.anonymiseTest();
        System.out.println("Divergence: " + kAnonymity.attributeDivergence());
        dataset.displayModifiedDataset();
        System.out.println("\nk: " + kAnonymity.getK());*/

        long startTime = System.currentTimeMillis();
        kAnonymity.anonymise();
        long ellapsedTime = System.currentTimeMillis();
        System.out.println("Ellapsed Time: " + (ellapsedTime / 1000.0) + " seconds.");
    }
}