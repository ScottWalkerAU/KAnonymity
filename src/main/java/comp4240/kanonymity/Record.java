package comp4240.kanonymity;

import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.attribute.IdentifierType;

import java.util.ArrayList;
import java.util.List;

public class Record {

    private List<Attribute> attributes = new ArrayList<>();
    private boolean suppressed;

    public Record() {
        this.suppressed = false;
    }

    public void addAttribute(Attribute att) {
        this.attributes.add(att);
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public boolean equivalentTo(Record other) {
        List<Attribute> otherAttributes = other.getAttributes();

        for (int i = 0; i < attributes.size(); i++) {
            Attribute a1 = attributes.get(i);
            Attribute a2 = otherAttributes.get(i);

            // Ensure we are only comparing the QIDs and not IDs or Sensitive Identifiers
            if (a1.getIdentifierType() != IdentifierType.QID) {
                continue;
            }

            if (!a1.equivalentTo(a2)) {
                return false;
            }
        }

        return true;
    }

    public void hardReset() {
        this.suppressed = false;
        resetModifiedValues();
    }

    public void resetModifiedValues() {
        for (Attribute a : attributes) {
            a.resetModifiedValue();
        }
    }

    public String getModifiedValues() {
        StringBuilder output = new StringBuilder();
        for (Attribute a : attributes) {
            output.append(a.getModifiedValue()).append('\t');
        }
        return output.toString();
    }

    public String getModifiedQIDValues() {
        StringBuilder output = new StringBuilder();
        for (Attribute a : getQIDs()) {
            // If the record is suppressed, use *
            String value = isSuppressed() ? "*" : a.getModifiedValue();
            output.append(value).append('\t');
        }
        return output.toString();
    }

    // TODO We never modify sensitive values though Harry?
    public String getModifiedSensitiveValues() {
        StringBuilder output = new StringBuilder();
        for (Attribute a : attributes) {
            if (a.getIdentifierType() == IdentifierType.SENSITIVE) {
                output.append(a.getModifiedValue()).append('\t');
            }
        }
        return output.toString();
    }

    public List<Attribute> getQIDs() {
        List<Attribute> qids = new ArrayList<>();
        for (Attribute a : attributes) {
            if (a.getIdentifierType() == IdentifierType.QID) {
                qids.add(a);
            }
        }
        return qids;
    }

    public boolean isSuppressed() {
        return suppressed;
    }

    public void setSuppressed() {
        this.suppressed = true;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Attribute a : attributes) {
            output.append(a.toString()).append('\t');
        }
        return output.toString();
    }
}
