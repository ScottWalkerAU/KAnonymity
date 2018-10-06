package comp4240.kanonymity.kanonymity;

import comp4240.kanonymity.Dataset;
import comp4240.kanonymity.attribute.Attribute;

public abstract class GeneralisationModel {

    private Dataset dataset;
    private String header;

    public GeneralisationModel(String header) {
        this.header = header;
    }

    // -- Abstract Methods --

    /**
     * Anonymise the attributes in the column
     */
    public abstract void anonymise();

    // -- General Methods --

    public void updateModifiedValue(Attribute attribute, String modifiedValue) {
        attribute.setModifiedValue(modifiedValue);
    }

    // -- Getters  --

    public Dataset getDataset() {
        return dataset;
    }

    public String getHeader() {
        return header;
    }

    // -- Setters --

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
