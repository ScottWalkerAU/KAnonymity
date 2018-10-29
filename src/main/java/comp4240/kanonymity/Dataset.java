// https://en.wikipedia.org/wiki/K-anonymity

package comp4240.kanonymity;

import comp4240.kanonymity.attribute.*;
import comp4240.kanonymity.tree.Range;
import comp4240.kanonymity.tree.Tree;
import comp4240.kanonymity.tree.TreeString;
import comp4240.kanonymity.tree.TreeRange;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

@Log4j2
public class Dataset {

    private List<String> headers;
    private int[] headerWidths;
    private List<IdentifierType> identifiers;
    private List<AttributeType> attributeTypes;
    private List<Record> records;
    private HashMap<String, Tree> generalisations;

    private List<Record> filtered;

    Dataset(String fileName, String taxonomyFileName) throws FileNotFoundException {
        this(fileName);
        loadTaxonomyTrees(taxonomyFileName);
    }

    Dataset(String fileName) throws FileNotFoundException {
        this.records = new ArrayList<>();
        this.generalisations = new HashMap<>();
        this.filtered = null;
        loadData(fileName);
    }

    private void loadData(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        String line;

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

        // Set the header widths, used for displaying the dataset
        setHeaderWidths(line.split(","));

        // Read in data
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            addRecord(line.split(","));
            setHeaderWidths(line.split(","));
        }
        scanner.close();
    }

    public void outputData(String fileName) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (String header : headers) {
            out.print(header + "\t");
        }

        for (Record r : records) {
            out.print("\n" + r.getModifiedValues());
        }

        out.close();
    }

    private void setHeaderWidths(String[] values) {
        if (headerWidths == null) {
            headerWidths = new int[identifiers.size()];
        }
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            headerWidths[i] = Math.max(headerWidths[i], value.length());
            headerWidths[i] = Math.max(headerWidths[i], 8);
        }
    }

    private void setHeaderWidths(int header, String[] values) {
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            headerWidths[header] = Math.max(headerWidths[header], value.length());
            headerWidths[header] = Math.max(headerWidths[header], 8);
        }
    }

    private void loadTaxonomyTrees(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);

        // Loop through the file line by line
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            // Check the line isn't empty
            if (line.trim().isEmpty()) {
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

            // Find out what index the header is in the data set to find the corresponding attributeType for the column.
            int headerIndex = headers.indexOf(header);
            if (headerIndex == -1) {
                throw new IllegalArgumentException("The taxonomy tree with the header '" + header + "' is not found within the dataset.");
            }

            // Update the widths of the columns used
            setHeaderWidths(headerIndex, values);

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
     * Index 0:  Header of the data set column used to reference the generalisation tree
     * Index 1:  The parent node
     * Index 2+: The children nodes that will be added to the parent node
     * @param values Input data as per description
     */
    private void addTaxonomyTreeNodeString(String[] values) {
        // Get the generalisation tree
        String header = values[0];
        TreeString tree = (TreeString) generalisations.get(header);

        // If the tree doesn't exist then create it
        if (tree == null) {
            tree = new TreeString(header);
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
     * Index 0:  Header of the data set column used to reference the generalisation tree
     * Index 1:  The parent node
     * Index 2+: The children nodes that will be added to the parent node
     * @param values Input data set per description
     */
    private void addTaxonomyTreeNodeRange(String[] values) {
        // Get the generalisation tree
        String header = values[0];
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

    private void addGeneralisation(Tree tree) {
        String attributeHeader = tree.getAttributeHeader();
        generalisations.put(attributeHeader, tree);
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
    private void setHeaders(String[] values) {
        headers = new ArrayList<>(values.length);
        for (String value : values) {
            headers.add(value.trim());
        }
    }

    private void addRecord(String[] values) {
        Record record = new Record();
        for (int i = 0; i < values.length; i++) {
            String value = values[i].trim();
            Attribute attribute;

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
                    throw new IllegalArgumentException("addRecord :: The Attribute Type: '" + attributeTypes.get(i) + "' is not recognised.");
            }

            record.addAttribute(attribute);
        }

        records.add(record);
    }

    /**
     * Reset modified values and if the record is suppressed
     */
    public void hardReset() {
        filtered = null;
        for (Record r : records) {
            r.hardReset();
        }
    }

    /**
     * Reset modified values. Not if the record is suppressed
     */
    public void resetModifiedValues() {
        for (Record r : records) {
            r.resetModifiedValues();
        }
    }

    /**
     * Loop through all records and collect the attributes from a header column
     * @param header Header to get attributes for
     * @return List of all attributes
     */
    public List<Attribute> getAttributes(String header) {
        int headerIndex = headers.indexOf(header);
        return getAttributes(headerIndex);
    }

    /**
     * Loop through all records and collect the attributes from a header column
     * @param column Column to get attributes for
     * @return List of all attributes
     */
    private List<Attribute> getAttributes(int column) {
        List<Attribute> attributes = new ArrayList<>();

        for (Record r : records) {
            List<Attribute> recordAttributes = r.getAttributes();
            attributes.add(recordAttributes.get(column));
        }

        return attributes;
    }

    public List<Record> getRecords() {
        if (filtered != null) {
            return filtered;
        }

        filtered = new ArrayList<>();
        for (Record r : records) {
            if (!r.isSuppressed()) {
                filtered.add(r);
            }
        }
        return filtered;
    }

    public int getSize() {
        return records.size();
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

    public long getTaxonomyTreeCombinations() {
        long combinations = 1;
        for (String header : headers) {
            Tree tree = generalisations.get(header);
            if (tree != null) {
                combinations *= tree.getTreeHeight() + 1;
            }
        }
        return combinations;
    }

    public HashMap<String, Integer> getEquivalenceClasses() {
        HashMap<String, Integer> equivalenceClasses = new HashMap<>();
        for (Record r : getRecords()) {
            String modifiedValues = r.getModifiedQIDValues();
            Integer size = equivalenceClasses.get(modifiedValues);
            size = (size == null) ? 1 : size + 1;
            equivalenceClasses.put(modifiedValues, size);
        }
        return equivalenceClasses;
    }

    public HashMap<String, List<Record>> getEquivalenceClassesRecords() {
        HashMap<String, List<Record>> equivalenceClasses = new HashMap<>();
        for (Record r : getRecords()) {
            String modifiedValues = r.getModifiedQIDValues();
            List<Record> list = equivalenceClasses.get(modifiedValues);
            if (list == null) {
                list = new ArrayList<>();
                equivalenceClasses.put(modifiedValues, list);
            }
            list.add(r);
        }
        return equivalenceClasses;
    }

    // Debug method
    String printEquivalenceClasses() {
        HashMap<String, Integer> equivalenceClasses = getEquivalenceClasses();

        StringBuilder builder = new StringBuilder("Equivalence Classes\n");
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            String format = "%-" + (headerWidths[i]+2) + "s ";
            builder.append(String.format(format, header));
        }
        builder.append('\n');

        Iterator<Map.Entry<String, Integer>> it = equivalenceClasses.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pair = it.next();
            it.remove();
            String value = pair.getKey();
            String[] values = value.split("\t");


            for (int j = 0; j < values.length; j++) {
                String format = "%-" + (headerWidths[j]+2) + "s ";
                builder.append(String.format(format, values[j]));
            }
            builder.append("\tEquivalence Class Size: ").append(pair.getValue()).append('\n');
        }
        return builder.toString();
    }

    String modifiedToCSV() {
        StringBuilder builder = new StringBuilder("The modified dataset\n");
        for (int i = 0; i < headers.size(); i++) {
            AttributeType attributeType = attributeTypes.get(i);
            builder.append(attributeType);
            builder.append(",");
        }
        builder.append('\n');

        for (String header : headers) {
            builder.append(header);
            builder.append(",");
        }
        builder.append('\n');

        for (Record r : records) {
            List<Attribute> attributes = r.getAttributes();
            for (Attribute attribute : attributes) {
                builder.append(attribute.getModifiedValue());
                builder.append(",");
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    String equivalenceToCSV() {
        HashMap<String, Integer> equivalenceClasses = getEquivalenceClasses();

        StringBuilder builder = new StringBuilder("Equivalence Classes\n");
        for (String header : headers) {
            builder.append(header);
            builder.append(",");
        }
        builder.append('\n');

        Iterator<Map.Entry<String, Integer>> it = equivalenceClasses.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pair = it.next();
            it.remove();
            String value = pair.getKey();
            String[] values = value.split("\t");


            for (String value1 : values) {
                builder.append(value1);
                builder.append(",");
            }
            builder.append("\tEquivalence Class Size: ").append(pair.getValue()).append('\n');
        }
        return builder.toString();
    }

    private List<String> getQIDHeaders() {
        List<String> QIDs = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            if (identifiers.get(i) == IdentifierType.QID) {
                QIDs.add(headers.get(i));
            }
        }
        return QIDs;
    }

    private HashMap<String, AttributeCount> getAttributeCounts(List<String> headerQIDs) {
        HashMap<String, AttributeCount> counts = new HashMap<>();
        for (Record r : records) {
            List<Attribute> recordQIDs = r.getQIDs();
            for (int i = 0; i < headerQIDs.size(); i++) {
                String qid = headerQIDs.get(i);
                AttributeCount counter = counts.get(qid);
                if (counter == null) {
                    counter = new AttributeCount(qid);
                    counts.put(qid, counter);
                }
                counter.add(recordQIDs.get(i).toString());
            }
        }
        return counts;
    }

    void suppressOutliers() {
        List<String> qids = getQIDHeaders();
        HashMap<String, AttributeCount> countMap = getAttributeCounts(qids);

        int suppressed = 0;
        for (int i = 0; i < qids.size(); i++) {
            String qid = qids.get(i);
            AttributeCount counts = countMap.get(qid);
            double mean = counts.getMean();
            double stdDev = counts.getStdDev();

            for (Record r : getRecords()) {
                if (r.isSuppressed()) {
                    continue;
                }

                Attribute attribute = r.getQIDs().get(i);
                int count = counts.getCounts().get(attribute.toString());
                if (count < mean - stdDev) {
                    r.setSuppressed();
                    suppressed++;
                }
            }
        }
        this.filtered = null;
        System.out.println("Suppressed " + suppressed + " records in the data set because they were identified as outliers!\n");
    }

    // -- Printers --

    public void displayDataset() {
        displayDataset(records.size());
    }

    void displayDataset(int amount) {
        StringBuilder builder = new StringBuilder("\nDisplaying the first '" + amount + "' of records in the data set.\n");
        for (int i = 0; i < headers.size(); i++) {
            AttributeType attributeType = attributeTypes.get(i);
            String format = "%-" + headerWidths[i] + "s ";
            builder.append(String.format(format, attributeType));
        }
        builder.append('\n');

        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            String format = "%-" + headerWidths[i] + "s ";
            builder.append(String.format(format, header));
        }
        builder.append('\n');

        for (int i = 0; i < Math.min(amount, records.size()); i++) {
            Record r = records.get(i);
            List<Attribute> attributes = r.getAttributes();
            for (int j = 0; j < attributes.size(); j++) {
                Attribute attribute = attributes.get(j);
                String format = "%-" + headerWidths[j] + "s ";
                builder.append(String.format(format, attribute.toString()));
            }
            builder.append('\n');
        }

        System.out.println(builder.toString());
    }

    public void displayModifiedDataset() {
        displayModifiedDataset(records.size());
    }

    public void displayModifiedDataset(int amount) {
        StringBuilder builder = new StringBuilder("\nDisplaying the first '" + amount + "' of records of the modified data set.\n");
        for (int i = 0; i < headers.size(); i++) {
            AttributeType attributeType = attributeTypes.get(i);
            String format = "%-" + headerWidths[i] + "s ";
            builder.append(String.format(format, attributeType));
        }
        builder.append('\n');

        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            String format = "%-" + headerWidths[i] + "s ";
            builder.append(String.format(format, header));
        }
        builder.append('\n');

        for (int i = 0; i < Math.min(amount, records.size()); i++) {
            Record r = records.get(i);
            List<Attribute> attributes = r.getAttributes();
            for (int j = 0; j < attributes.size(); j++) {
                Attribute attribute = attributes.get(j);
                String format = "%-" + headerWidths[j] + "s ";
                builder.append(String.format(format, attribute.getModifiedValue()));
            }
            builder.append('\n');
        }
        System.out.println(builder.toString());
    }
}