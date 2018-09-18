package comp4240.kanonymity.kanonymity;

import comp4240.kanonymity.Dataset;
import comp4240.kanonymity.attribute.Attribute;

import java.util.List;

public abstract class GeneralisationModel {

    private Dataset dataset;
    private int desiredK;

    // -- Abstract methods --

    /**
     * Anonymise the entire dataset all to the same generalisation level.
     */
    public abstract void anonymise();

    /**
     * Only anonymise the attributes in the column
     * @param header    The name of the dataset column that wishes to be anonymised.
     */
    public abstract void anonymise(String header);

    /**
     * Only anonymises the attributes passed through as a list.
     * @param header        The column in which the attributes have been pulled from.
     * @param attributes    The list of attributes to be anonymised.
     */
    public abstract void anonymise(String header, List<Attribute> attributes);

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

    public void updateModifiedValue(Attribute attribute, String modifiedValue) {
        attribute.setModifiedValue(modifiedValue);
    }
}
