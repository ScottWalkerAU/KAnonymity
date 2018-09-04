package comp4240.kanonymity;

public class Main {

    private int k;
    private String fileName;

    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        new Main("dataK2.txt");
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
        dataset.AttributeDivergence();
    }
}