package comp4240.kanonymity;

import comp4240.kanonymity.kanonymity.KAnonymity;
import comp4240.kanonymity.tree.TreeDefault;

public class Main {

    private String fileName;

    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        new Main("data.txt");
    }

    Main(String fileName) {
        this.fileName = fileName;

        Dataset dataset = new Dataset(fileName);
        dataset.displayDataset();

        // Define the Occupation hierarchy
        TreeDefault occupation = new TreeDefault("Occupation");

        // Add the different levels of generalisation
        occupation.add("ANY", "Engineer", "Science");
        occupation.add("Engineer", "Software", "Chemical");
        occupation.add("Science", "Physics", "Chemistry");

        // Show the tree contents
        System.out.println("\nOccupation Tree printout:");
        occupation.getRoot().printAsArray();

        // Add the generalisations to the dataset
        dataset.addGeneralisation(occupation);

        // Create the K-Anonymity class
        KAnonymity kAnonymity = new KAnonymity(dataset, 2);

        // Print the results
        System.out.println("\nGeneralised values:");
        kAnonymity.anonymise();
    }
}