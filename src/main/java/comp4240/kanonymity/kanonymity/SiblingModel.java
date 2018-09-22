package comp4240.kanonymity.kanonymity;

import comp4240.kanonymity.Dataset;
import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.tree.Node;
import comp4240.kanonymity.tree.Tree;

import java.util.List;

public class SiblingModel extends GeneralisationModel {

    private Node parent;
    private Node child;

    public SiblingModel(String header, Node parent, Node child) {
        super(header);
        this.parent = parent;
        this.child = child;
    }

    /**
     * {@inheritDoc}
     */
    public void anonymise() {
        Dataset dataset = getDataset();
        List<Attribute> attributes = dataset.getAttributes(getHeader());
        Tree generalisationTree = dataset.getGeneralisationTree(getHeader());

        // No tree? Just return without doing anything
        if (generalisationTree == null) {
            return;
        }

        // Loop through all attributes
        for (Attribute attribute : attributes) {
            Node current = generalisationTree.getNode(attribute);

            // Not a descendant, ignore it.
            if (!current.canGeneraliseTo(child)) {
                continue;
            }

            // Calculate the generalised value
            String originalValue = attribute.toString();
            String generalisedValue = parent.getValue();

            // Update the modified values
            updateModifiedValue(attribute, generalisedValue);

            // Output the new generalised value
            System.out.printf("%s is generalised to %s when using sibling model\n", originalValue, generalisedValue);
        }
    }
}
