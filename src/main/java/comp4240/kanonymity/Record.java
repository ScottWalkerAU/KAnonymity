package comp4240.kanonymity;

import comp4240.kanonymity.attribute.Attribute;

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

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Attribute a : attributes) {
            output.append(a.toString()).append('\t');
        }
        return output.toString();
    }
}
