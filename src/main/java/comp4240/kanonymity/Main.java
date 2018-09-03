package comp4240.kanonymity;

public class Main {

    private int k;
    private String fileName = "dataK4.txt";

    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        new Main();
    }

    Main() {
        k = 2;
        Dataset dataset = new Dataset(fileName);
        dataset.displayDataset();
        boolean valid = dataset.isKAnonimity(k);
        System.out.println("K-Anonymity k: " + k + " = " + valid);
        int currentK = dataset.getK();
        System.out.println("K-Anonymity dataset current k is: " + currentK);

    }
}