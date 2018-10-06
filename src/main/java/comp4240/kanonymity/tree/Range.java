package comp4240.kanonymity.tree;

public class Range {
    private Integer min, max;

    public Range(Integer min, Integer max) {
        setMin(min);
        setMax(max);
    }

    /**
     * Takes a string version of the range in the format of [0..50] or [0..null] for an open upper range.
     * @param range The range string
     */
    public Range(String range) {
        // [0..50] is the format

        // Replaces the "[" & "]" at the end of the range
        range = range.replaceAll("[\\[\\]]", "");

        // Split the range at ".."
        String[] values = range.split("\\.{2}");
        Integer min, max;

        // Set the min value to either null or a value;
        if (values[0].equals("*")) {
            min = null;
        } else {
            min = Integer.parseInt(values[0]);
        }

        // Set the max value to either null or a value;
        if (values[1].equals("*")) {
            max = null;
        } else {
            max = Integer.parseInt(values[1]);
        }

        // Set the values
        setMin(min);
        setMax(max);
    }

    // -- General Methods --

    public boolean contains(Integer value) {
        if (hasLowerBound() && value < min) {
            return false;
        }

        if (hasUpperBound() && value > max) {
            return false;
        }

        return true;
    }

    public boolean equals(Range other) {
        return other.getMin() == this.getMin() && other.getMax() == this.getMax();
    }

    public String toString() {
        String out = "[";

        out += (min == null) ? "*" : min;
        out += ", ";
        out += (max == null) ? "*" : max;
        out += "]";

        return out;
    }

    public boolean hasLowerBound() {
        return min != null;
    }

    public boolean hasUpperBound() {
        return max != null;
    }

    public static boolean isRange(String range) {
        if (range.contains("[") && range.contains("]") && range.contains(",")) {
            return true;
        }

        return false;
    }

    // -- Getters --

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    // -- Setters --

    public void setMin(Integer min) {
        this.min = min;
    }

    public void setMax(Integer max) {
        this.max = max;
    }
}
