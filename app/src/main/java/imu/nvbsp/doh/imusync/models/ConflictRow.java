package imu.nvbsp.doh.imusync.models;

public class ConflictRow {
    private String attr;
    private String leftValue;
    private String rightValue;

    public ConflictRow(String attr, String leftValue, String rightValue) {
        this.attr = attr;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public String getAttr() {
        return attr;
    }

    public String getLeftValue() {
        return leftValue;
    }

    public String getRightValue() {
        return rightValue;
    }
}
