package comp4240.kanonymity;

import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.tree.TreeDefault;

import java.util.List;

public class Main {

    private int k;
    private String fileName;

    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        new Main("dataNameGenderAgeStudyDisease.txt");
    }

    Main(String fileName) {
        this.k = 2;
        this.fileName = fileName;

        Dataset dataset = new Dataset(fileName);
        dataset.displayDataset();
        boolean valid = dataset.isKAnonymous(k);
        System.out.println("K-Anonymity k: " + k + " = " + valid);
        int currentK = dataset.getK();
        System.out.println("K-Anonymity dataset current k is: " + currentK);
        // dataset.AttributeDivergence();

        List<Attribute> attributes = dataset.getAttributes(1);

        // Define the Occupation hierarchy
        TreeDefault occupation = new TreeDefault();

        // Add the different levels of generalisation
        occupation.add("ANY", "Engineer", "Science");
        occupation.add("Engineer", "Software", "Chemical");
        occupation.add("Science", "Physics", "Chemistry");

        occupation.getRoot().printAsArray();
    }
}