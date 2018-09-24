package comp4240.kanonymity;

import comp4240.kanonymity.kanonymity.KAnonymity;
import comp4240.kanonymity.tree.Range;
import comp4240.kanonymity.tree.TreeDefault;
import comp4240.kanonymity.tree.TreeRange;

public class Main {

    private String fileName;

    public static void main(String[] args) {
        new Main("data.txt", "dataTaxonomy.txt");
    }

    private Main(String... files) {
        this.fileName = files[0];
        Dataset dataset;

        if (files.length > 1) {
            dataset = new Dataset(fileName, files[1]);
        } else {
            dataset = new Dataset(fileName);
        }

        dataset.displayDataset();

        // Create the K-Anonymity class
        KAnonymity kAnonymity = new KAnonymity(dataset, 2);

        // Print the results
        /*System.out.println("\nGeneralised values:");
        kAnonymity.anonymiseTest();
        System.out.println("Divergence: " + kAnonymity.attributeDivergence());
        dataset.displayModifiedDataset();
        System.out.println("\nk: " + kAnonymity.getK());*/

        kAnonymity.anonymise();
    }
}