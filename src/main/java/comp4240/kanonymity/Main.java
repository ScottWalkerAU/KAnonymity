package comp4240.kanonymity;

import comp4240.kanonymity.kanonymity.KAnonymity;
import comp4240.kanonymity.kanonymity.LDiversity;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {

    private String fileName;
    private Dataset dataset;

    public static void main(String[] args) {
        new Main("data/CensusData1000.csv", "data/CensusDataTaxonomy.txt").run();
    }

    private Main(String... files) {
        this.fileName = files[0];

        if (files.length > 1) {
            dataset = new Dataset(fileName, files[1]);
        } else {
            dataset = new Dataset(fileName);
        }
    }

    private void run() {
        long startTime = System.currentTimeMillis();
        dataset.suppressOutliers();

        log.debug("The number of combinations for the loaded taxonomy trees is " + dataset.getTaxonomyTreeCombinations() + " combinations");

        dataset.displayDataset(10);

        // Create the K-Anonymity class
        KAnonymity kAnonymity = new KAnonymity(dataset, 2);

        // Create the L-Diverse class
        //KAnonymity kAnonymity = new LDiversity(dataset, 1, 1);

        // Anonymise the data set
        kAnonymity.anonymise();

        dataset.printEquivalenceClasses();
        kAnonymity.printStats();

        long elapsedTime = System.currentTimeMillis() - startTime;
        log.debug("Elapsed Time: " + (elapsedTime / 1000.0) + " seconds");
    }

}