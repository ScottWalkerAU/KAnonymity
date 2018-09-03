package comp4240.kanonymity;

import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.attribute.IdentifierType;

import java.util.ArrayList;
import java.util.List;

public class Record {

    private String header;
    private List<Attribute> attributes = new ArrayList<>();

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void addAttribute(Attribute att) {
        this.attributes.add(att);
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public boolean equivalentTo(Record record) {
        List<Attribute> recordAttributes = record.getAttributes();

        for (int i = 0; i < attributes.size(); i++) {
            Attribute a1 = attributes.get(i);
            Attribute a2 = recordAttributes.get(i);

            // Ensure we are only comparing the QID's and not ID's or Sensitive Identifiers
            if (a1.geIdentifierType() != IdentifierType.QID) {
                continue;
            }

            if (!a1.equivalentTo(a2)) {
                return false;
            }
        }

        return true;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Attribute a : attributes) {
            output.append(a.toString()).append('\t');
        }
        return output.toString();
    }
}
