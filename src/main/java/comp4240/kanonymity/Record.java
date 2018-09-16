package comp4240.kanonymity;

import comp4240.kanonymity.attribute.Attribute;
import comp4240.kanonymity.attribute.IdentifierType;

import java.util.ArrayList;
import java.util.List;

public class Record {

    private List<Attribute> attributes = new ArrayList<>();

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

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Attribute a : attributes) {
            output.append(a.toString()).append('\t');
        }
        return output.toString();
    }
}
