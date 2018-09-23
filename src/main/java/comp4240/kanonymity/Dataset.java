// https://en.wikipedia.org/wiki/K-anonymity

package comp4240.kanonymity;

import comp4240.kanonymity.attribute.*;
import comp4240.kanonymity.tree.Range;
import comp4240.kanonymity.tree.Tree;
import comp4240.kanonymity.tree.TreeDefault;
import comp4240.kanonymity.tree.TreeRange;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Dataset {

    private List<String> headers;
    private List<IdentifierType> identifiers;
    private List<AttributeType> attributeTypes;
    private List<Record> records;
    private HashMap<String, Tree> generalisations;

    public Dataset(String fileName, String taxonomyFileName) {
        this(fileName);
        loadTaxonomyTrees(taxonomyFileName);
    }

    public Dataset(String fileName) {
        records = new ArrayList<>();
        generalisations = new HashMap<>();
        loadData(fileName);
    }

    public void loadData(String path) {
        System.out.println("[INFO]   loadData   Loading data");
        Scanner scanner;
        String line;

        try {
            File file = new File(path);
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Check the files not blank
        if (!scanner.hasNext()) {
            return;
        }

        // Identifier Type
        line = scanner.nextLine();
        setIdentifierType(line.split(","));

        // Attribute Type
        line = scanner.nextLine();
        setAttributeTypes(line.split(","));

        // Headers
        line = scanner.nextLine();
        setHeaders(line.split(","));

        // Data
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            addRecord(line.split(","));
        }
        scanner.close();
    }

    public void loadTaxonomyTrees(String path) {
        System.out.println("[INFO]   loadTaxonomyTrees   Loading TaxonomyTrees");
        Scanner scanner;
        String line;

        try {
            File file = new File(path);
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Check the files not blank
        if (!scanner.hasNext()) {
            return;
        }

        // Loop through the file line by line
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            // Check the line isn't empty
            if (line.isEmpty()) {
                continue;
            }

            // Split the line by commas
            String[] values = line.split(",");

            // Trim all the results in the values
            for (int i = 0; i < values.length; i++) {
                values[i] = values[i].trim();
            }

            // Get the header for the taxonomy tree
            String header = values[0];

            // Find out what index the header is in the data set to find the corrisponding attributeType for the column.
            int headerIndex = headers.indexOf(header);
            AttributeType attributeType = attributeTypes.get(headerIndex);

            // Depending on the attribute type call each respective function.
            switch (attributeType) {
                case STRING:
                    addTaxonomyTreeNodeString(values);
                    break;
                case NUMERIC:
                    addTaxonomyTreeNodeRange(values);
                    break;
            }
        }
    }

    /**
     * Given an array of values:
     * Index 0:     Header of the data set column used to reference the generalisation tree
     * Index 1:     The parent node
     * Index 2+:    The children nodes that will be added to the parent node
     * @param values
     */
    private void addTaxonomyTreeNodeString(String[] values) {
        // Header of the data set column
        String header = values[0];

        // Get the generalisation tree
        TreeDefault tree = (TreeDefault) generalisations.get(header);

        // If the tree doesn't exist then create it
        if (tree == null) {
            tree = new TreeDefault(header);
            addGeneralisation(tree);
        }

        // Get the parent node
        String parent = values[1];

        // Add all the children to that node
        for (int i = 2; i < values.length; i++) {
            tree.add(parent, values[i]);
        }
    }

    /**
     * Given an array of values:
     * Index 0:     Header of the data set column used to reference the generalisation tree
     * Index 1:     The parent node
     * Index 2+:    The children nodes that will be added to the parent node
     * @param values
     */
    private void addTaxonomyTreeNodeRange(String[] values) {
        // Header of the data set column
        String header = values[0];

        // Get the generalisation tree
        TreeRange tree = (TreeRange) generalisations.get(header);

        // If the tree doesn't exist then create it
        if (tree == null) {
            tree = new TreeRange(header);
            addGeneralisation(tree);
        }

        // Get the parent node
        Range parent = new Range(values[1]);

        // Add all the children to that node
        for (int i = 2; i < values.length; i++) {
            Range child = new Range(values[i]);
            tree.add(parent, child);
        }
    }

    private void setIdentifierType(String[] values) {
        identifiers = new ArrayList<>(values.length);

        for (String value : values) {
            value = value.trim();
            IdentifierType type = IdentifierType.getType(value);
            identifiers.add(type);
        }
    }

    /**
     * Takes an array of Strings and converts the values to an enum value to set the attribute types of each column.
     * @param values the array containing all the attribute types for each column.
     */
    private void setAttributeTypes(String[] values) {
        attributeTypes = new ArrayList<>(values.length);

        for (String value : values) {
            value = value.trim();
            AttributeType type = AttributeType.getType(value);
            attributeTypes.add(type);
        }
    }

    /**
     * Takes an array of Strings and sets the headers to the corresponding values
     * @param values the array containing the header values.
     */
    public void setHeaders(String[] values) {
        headers = new ArrayList<>(values.length);
        for (String value : values) {
            headers.add(value.trim());
        }
    }

    public void addRecord(String[] values) {
        Record record = new Record();
        for (int i = 0; i < values.length; i++) {
            String value = values[i].trim();
            Attribute attribute = null;

            switch (attributeTypes.get(i)) {
                case STRING:
                    attribute = new StringAttribute(value, identifiers.get(i));
                    break;
                case NUMERIC:
                    Integer v = Integer.parseInt(value);
                    attribute = new NumericAttribute(v, identifiers.get(i));
                    break;
                case BINARY:
                    attribute = new BinaryAttribute(value, identifiers.get(i));
                    break;
                case DATE:
                    attribute = new DateAttribute(value, identifiers.get(i));
                    break;
                default:
                    System.out.println("{WARNING]   addRecord   The Attribute Type: '" + attributeTypes.get(i) + "' is not recognised.");
                    break;
            }

            record.addAttribute(attribute);
        }

        records.add(record);
    }

    public void resetModifiedValues() {
        for (Record r : records) {
            r.resetModifiedValues();
        }
    }

    /**
     * Loop through all records and collect the attributes from a header column
     * @param header
     * @return
     */
    public List<Attribute> getAttributes(String header) {
        int headerIndex = headers.indexOf(header);

        return getAttributes(headerIndex);
    }

    public List<Attribute> getAttributes(int column) {
        List<Attribute> attributes = new ArrayList<>();

        for (Record r : records) {
            List<Attribute> recordAttributes = r.getAttributes();
            attributes.add(recordAttributes.get(column));
        }

        return attributes;
    }

    public void displayDataset() {

        System.out.println("The Dataset");
        for (AttributeType attributeType : attributeTypes) {
            System.out.print(attributeType + "\t");
        }
        System.out.println();

        for (String header : headers) {
            System.out.print(header + "\t");
        }
        System.out.println();

        for (Record r : records) {
            System.out.println(r);
        }
    }

    public void displayModifiedDataset() {

        System.out.println("\nThe Modified Dataset");
        for (AttributeType attributeType : attributeTypes) {
            System.out.print(attributeType + "\t");
        }
        System.out.println();

        for (String header : headers) {
            System.out.print(header + "\t");
        }
        System.out.println();

        for (Record r : records) {
            System.out.println(r.getModifiedValues());
        }
    }

    public void addGeneralisation(Tree tree) {
        String attributeHeader = tree.getAttributeHeader();
        generalisations.put(attributeHeader, tree);
    }

    public List<Record> getRecords() {
        return records;
    }

    public List<IdentifierType> getIdentifiers() {
        return identifiers;
    }

    public int getAttributeSize() {
        return attributeTypes.size();
    }

    public List<String> getHeaders() {
        return headers;
    }

    public Tree getGeneralisationTree(String key) {
        return generalisations.get(key);
    }

    public List<Tree> getGeneralisations() {
        return new ArrayList<>(generalisations.values());
    }

}