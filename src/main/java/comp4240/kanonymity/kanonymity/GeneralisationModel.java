package comp4240.kanonymity.kanonymity;

import comp4240.kanonymity.Dataset;

public abstract class GeneralisationModel {

    private Dataset dataset;
    private int desiredK;

    // -- Abstract methods --

    public abstract void anonymise();

    // -- Getters  --

    public Dataset getDataset() {
        return dataset;
    }

    public int getDesiredK() {
        return desiredK;
    }

    // -- Setters --

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public void setDesiredK(int desiredK) {
        this.desiredK = desiredK;
    }
}
