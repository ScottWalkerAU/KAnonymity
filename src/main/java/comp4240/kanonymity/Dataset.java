// https://en.wikipedia.org/wiki/K-anonymity

package comp4240.kanonymity;

import comp4240.kanonymity.attribute.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dataset {

    private String[] headers;
    private AttributeType[] attributeTypes;
    private ArrayList<Record> records = new ArrayList<>();

    public Dataset() {
        loadData("data.txt");
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

        // Attribute Type
        System.out.println("[INFO]   loadData   Loading Attribute Types");
        line = scanner.nextLine();
        setAttributeTypes(line.split(","));

        // Headers
        System.out.println("[INFO]   loadData   Loading Headers");
        line = scanner.nextLine();
        setHeaders(line.split(","));

        // Data
        System.out.println("[INFO]   loadData   Loading Data");
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            addRecord(line.split(","));
        }
        scanner.close();
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
                    attributeTypes[i] = AttributeType.NOMINAL;
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
                    attribute = new StringAttribute(value);
                    break;
                case NOMINAL:
                    double v = Double.parseDouble(value);
                    attribute = new NominalAttribute(v);
                    break;
                case BINARY:
                    attribute = new BinaryAttribute(value);
                    break;
                case DATE:
                    attribute = new DateAttribute(value);
                    break;
                default:
                    System.out.println("{WARNING]   addRecord   The Attribute Type: '" + attributeTypes[i] + "' is not recognised.");
                    break;
            }

            record.addAttribute(attribute);
        }

        records.add(record);
    }

    public void displayDataset() {
        for (AttributeType attributeType : attributeTypes) {
            System.out.print(attributeType + "\t");
        }
        System.out.println();

        for (Record r : records) {
            System.out.println(r);
        }
    }
}