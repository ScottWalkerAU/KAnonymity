package comp4240.kanonymity;

import java.util.HashMap;

public class AttributeCount {

    private String header;
    private HashMap<String, Integer> counts;

    public AttributeCount(String header) {
        this.header = header;
        this.counts = new HashMap<>();
    }

    public void add(String value) {
        Integer count = counts.get(value);
        count = (count == null) ? 1 : count + 1;
        counts.put(value, count);
    }

    public double getMean() {
        double sum = 0.0;
        for (Integer count : counts.values()) {
            sum += count;
        }
        return sum / counts.size();
    }

    public double getVariance() {
        double mean = getMean();
        double temp = 0;
        for (Integer count : counts.values()) {
            temp += Math.pow(count - mean, 2);
        }
        return temp / (counts.size() - 1);
    }

    public double getStdDev() {
        return Math.sqrt(getVariance());
    }

    // -- Getters --

    public String getHeader() {
        return header;
    }

    public HashMap<String, Integer> getCounts() {
        return counts;
    }
}