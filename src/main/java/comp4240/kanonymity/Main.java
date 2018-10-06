package comp4240.kanonymity;

import comp4240.kanonymity.kanonymity.KAnonymity;
import lombok.extern.log4j.Log4j2;

import java.io.FileNotFoundException;

@Log4j2
public class Main {

    /** Dataset we're using */
    private Dataset dataset;

    public static void main(String[] args) {
        try {
            new Main("data/CensusData1000.csv", "data/CensusDataTaxonomy.txt").run();
        } catch (FileNotFoundException e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    private Main(String fileName, String... trees) throws FileNotFoundException {
        if (trees.length > 0) {
            dataset = new Dataset(fileName, trees[0]);
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