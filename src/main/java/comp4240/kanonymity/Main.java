package comp4240.kanonymity;

import comp4240.kanonymity.kanonymity.KAnonymity;
import comp4240.kanonymity.tree.Range;
import comp4240.kanonymity.tree.TreeDefault;
import comp4240.kanonymity.tree.TreeRange;

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
        occupation.add("Engineer", "Software", "Surveying");
        occupation.add("Science", "Physics", "Chemistry");

        occupation.add("Software", "Comp Sci", "Soft Eng");

        TreeRange age = new TreeRange("Age");
        Range root = new Range(0, 50), a = new Range(0, 24), b = new Range(25, 50);
        Range aa = new Range(0,12), ab = new Range(13, 24), ba = new Range(25, 37), bb = new Range(38, 50);
        age.add(root, a, b);
        age.add(a, aa, ab);
        age.add(b, ba, bb);

        // Show the tree contents
        System.out.println("\nAge Tree printout:");
        //occupation.getRoot().printAsArray();
        age.getRoot().printAsArray();

        // Add the generalisations to the dataset
        dataset.addGeneralisation(occupation);
        dataset.addGeneralisation(age);

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