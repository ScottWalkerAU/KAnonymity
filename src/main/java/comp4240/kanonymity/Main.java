package comp4240.kanonymity;

public class Main {

    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        new Main();
    }

    Main() {
        Dataset dataset = new Dataset();
        dataset.displayDataset();
    }
}