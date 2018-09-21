package comp4240.kanonymity.tree;

public class Range {
    private Integer min, max;

    public Range(Integer min, Integer max) {
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
        if (other.getMin() == this.getMin() && other.getMax() == this.getMax()) {
            return true;
        }

        return false;
    }

    public String toString() {
        return "[" + min + ", " + max + "]";
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
