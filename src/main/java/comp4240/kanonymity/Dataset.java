// https://en.wikipedia.org/wiki/K-anonymity

package comp4240.kanonymity;

import comp4240.kanonymity.attribute.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dataset {

    private String[] headers;
    private IdentifierType[] identifiers;
    private AttributeType[] attributeTypes;
    private List<Record> records = new ArrayList<>();

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
            String identifierType = values[i].toLowerCase();

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
            String attributeType = values[i].toLowerCase();

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
        System.out.println("[INFO]   setHeaders");
        headers = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            headers[i] = values[i];
        }
    }

    public void addRecord(String[] values) {
        Record record = new Record();
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
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

    /**
     * Giving a size k, the method will return whether of not the dataset is k-anonymised
     * @param k the size of k
     * @return true of false if the dataset is k-anonymised
     */
    public boolean isKAnonymous(int k) {
        int minK = records.size();

        // For each record check if there are at least k matches
        for (int i = 0; i < records.size(); i++) {
            int matches = 0;
            Record r1 = records.get(i);

            // Check all other records for a match
            for (int j = 0; j < records.size(); j++) {
                Record r2 = records.get(j);

                if (r1.equivalentTo(r2)) {
                    matches++;
                }
            }

            if (k < minK) {
                minK = k;
            }

            if (matches < k) {
                return false;
            }
        }

        return true;
    }

    /**
     * Calculates the size of k from the current dataset.
     * @return the size of k.
     */
    public int getK() {
        int minK = records.size();

        // For each record check if there are at least k matches
        for (int i = 0; i < records.size(); i++) {
            int matches = 0;
            Record r1 = records.get(i);

            // Check all other records for a match
            for (int j = 0; j < records.size(); j++) {
                Record r2 = records.get(j);

                if (r1.equivalentTo(r2)) {
                    matches++;
                }
            }

            // Can't get any worse than 1, not reason to keep looking
            if (matches == 1) {
                return 1;
            }

            if (matches < minK) {
                minK = matches;
            }
        }

        return minK;
    }

    public void AttributeDivergence() {
        System.out.println("\nAttribute Divergence");
        // Loop through all records one Attribute column at a time
        for (int i = 0; i < headers.length; i++) {
            List<String> values = new ArrayList<>();

            // Record all the different variable types
            for (Record r : records) {
                List<Attribute> recordAttributes = r.getAttributes();
                Attribute a = recordAttributes.get(i);
                String value = a.toString();

                if (!values.contains(value)) {
                    values.add(value);
                }
            }
            // Calculate how many different results are found
            System.out.println(headers[i] + ": Unique values: " + values.size() + ", percentage of data is " + (100 * records.size() / values.size() / records.size()) + "%");
        }
    }
}