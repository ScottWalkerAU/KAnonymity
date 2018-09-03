package comp4240.kanonymity;

import comp4240.kanonymity.attribute.Attribute;

import java.util.ArrayList;
import java.util.List;

public class Record {
    private String header;
    private List<Attribute> attributes = new ArrayList<>();

    public void setHeader(String header) { this.header = header; }
    public String getHeader() { return this.header; }
    public void addAttribute(Attribute att) { this.attributes.add(att); }
    public List<Attribute> getAttributes() { return this.attributes; }

    public String toString() {
        String output = "";

        for (Attribute a : attributes) {
            output += a.toString() + "\t";
        }

        return output;
    }
}
