// https://en.wikipedia.org/wiki/K-anonymity

package comp4240.kanonymity;

import comp4240.kanonymity.attribute.*;
import comp4240.kanonymity.tree.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Dataset {

    private List<String> headers;
    private IdentifierType[] identifiers;
    private AttributeType[] attributeTypes;
    private List<Record> records = new ArrayList<>();
    private HashMap<String, Tree> generalisations = new HashMap<>();

    public Dataset(String fileName) {
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

    public void setIdentifierType(String[] values) {
        System.out.println("[INFO]   setIdentifierType");
        identifiers = new IdentifierType[values.length];

        for (int i = 0; i < values.length; i++) {
            String identifierType = values[i].toLowerCase().trim();

            switch (identifierType) {
                case "id":
                    identifiers[i] = IdentifierType.ID;
                    break;
                case "qid":
                    identifiers[i] = IdentifierType.QID;
                    break;
                case "sensitive":
                    identifiers[i] = IdentifierType.SENSITIVE;
                    break;
                default:
                    System.out.println("{WARNING]   setIdentifierType   The Identifier Type: '" + identifierType + "' is not recognised.");
                    break;
            }
        }
    }

    /**
     * Takes an array of Strings and converts the values to an enum value to set the attribute types of each column.
     * @param values the array containing all the attribute types for each column.
     */
    public void setAttributeTypes(String[] values) {
        System.out.println("[INFO]   setAttributeTypes");
        attributeTypes = new AttributeType[values.length];

        for (int i = 0; i < values.length; i++) {
            String attributeType = values[i].toLowerCase().trim();

            switch (attributeType) {
                case "string":
                    attributeTypes[i] = AttributeType.STRING;
                    break;
                case "nominal":
                    attributeTypes[i] = AttributeType.NUMERIC;
                    break;
                case "binary":
                    attributeTypes[i] = AttributeType.BINARY;
                    break;
                case "date":
                    attributeTypes[i] = AttributeType.DATE;
                    break;
                default:
                    System.out.println("{WARNING]   setAttributeTypes   The Attribute Type: '" + attributeType + "' is not recognised.");
                    break;
            }
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

            switch (attributeTypes[i]) {
                case STRING:
                    attribute = new StringAttribute(value, identifiers[i]);
                    break;
                case NUMERIC:
                    double v = Double.parseDouble(value);
                    attribute = new NumericAttribute(v, identifiers[i]);
                    break;
                case BINARY:
                    attribute = new BinaryAttribute(value, identifiers[i]);
                    break;
                case DATE:
                    attribute = new DateAttribute(value, identifiers[i]);
                    break;
                default:
                    System.out.println("{WARNING]   addRecord   The Attribute Type: '" + attributeTypes[i] + "' is not recognised.");
                    break;
            }

            record.addAttribute(attribute);
        }

        records.add(record);
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

    public void addGeneralisation(Tree tree) {
        String attributeHeader = tree.getAttributeHeader();
        generalisations.put(attributeHeader, tree);
    }

    public List<Record> getRecords() {
        return records;
    }

    public int getAttributeSize() {
        return headers.size();
    }

    public List<String> getHeaders() {
        return headers;
    }

    public Tree getGeneralisationTree(String key) {
        return generalisations.get(key);
    }
}