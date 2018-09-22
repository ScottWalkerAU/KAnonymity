package comp4240.kanonymity.kanonymity;

import comp4240.kanonymity.Dataset;
import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.tree.Node;
import comp4240.kanonymity.tree.Tree;

import java.util.List;

public class SubtreeModel extends GeneralisationModel {

    private Node node;

    public SubtreeModel(String header, Node node) {
        super(header);
        this.node = node;
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
            if (!current.canGeneraliseTo(node)) {
                continue;
            }

            // Calculate the generalised value
            String originalValue = attribute.toString();
            String generalisedValue = node.getValue();

            // Update the modified values
            updateModifiedValue(attribute, generalisedValue);

            // Output the new generalised value
            System.out.printf("%s is generalised to %s when using subtree model\n", originalValue, generalisedValue);
        }
    }
}