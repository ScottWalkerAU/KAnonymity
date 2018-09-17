package comp4240.kanonymity;

import comp4240.kanonymity.kanonymity.KAnonymity;
import comp4240.kanonymity.tree.TreeDefault;
import comp4240.kanonymity.tree.TreeRange;
import org.apache.commons.lang3.Range;

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
        occupation.add("ANY", "Engineer", "Science");
        occupation.add("Engineer", "Software", "Chemical");
        occupation.add("Science", "Physics", "Chemistry");

        TreeRange age = new TreeRange("Age");
        Range root = Range.between(0, 50), a = Range.between(0,24), b = Range.between(25, 50);
        Range aa = Range.between(0,12), ab = Range.between(13, 24), ba = Range.between(25, 37), bb = Range.between(38, 50);
        age.add(root, a, b);
        age.add(a, aa, ab);
        age.add(b, ba, bb);

        // Show the tree contents
        System.out.println("\nOccupation Tree printout:");
        occupation.getRoot().printAsArray();

        // Add the generalisations to the dataset
        dataset.addGeneralisation(occupation);
        dataset.addGeneralisation(age);

        // Create the K-Anonymity class
        KAnonymity kAnonymity = new KAnonymity(dataset, 2);

        // Print the results
        System.out.println("\nGeneralised values:");
        kAnonymity.anonymise();
        dataset.displayModifiedDataset();
    }
}