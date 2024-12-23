package hk.edu.polyu.comp.comp2021.cvfs.model;

/**
 * This class represents a criterion
 */
public class SimpleCriterion implements Criterion {
    private String attrName;
    private String op;
    private String val;

    /**
     * Constructs a new SimpleCriterion with the following parameters
     *
     * @param attrName the name of the attribute to evaluate
     * @param op the operation to apply
     * @param val the value to compare against
     * @throws IllegalArgumentException if the parameters do not meet the specified requirements
     */
    public SimpleCriterion(String attrName, String op, String val) {
        if (attrName == null || op == null || val == null || !isValid(attrName, op, val)) {
            throw new IllegalArgumentException("Invalid criterion parameters");
        }
        this.attrName = attrName;
        this.op = op;
        this.val = val;
    }

    private boolean isValid(String attrName, String op, String val) {
        switch (attrName) {
            case "name":
                return "\"contains\"".equals(op) && val.startsWith("\"") && val.endsWith("\"");
            case "type":
                return "\"equals\"".equals(op) && val.startsWith("\"") && val.endsWith("\"");
            case "size":
                return op.matches("\"[><=!]=?\"") && val.matches("\\d+");
            default:
                return false;
        }
    }

    @Override
    public boolean evaluate(Object file) {
        if (file instanceof Document) {
            Document doc = (Document) file;
            switch (attrName) {
                case "name":
                    return evaluateString(doc.getName(), op, val);
                case "type":
                    return evaluateString(doc.getType(), op, val);
                case "size":
                    return evaluateInt(doc.getSize(), op, Integer.parseInt(val));
            }
        } else if (file instanceof Directory) {
            Directory dir = (Directory) file;
            if ("name".equals(attrName)) {
                return evaluateString(dir.getName(), op, val);
            } else if ("size".equals(attrName)) {
                return evaluateInt(dir.getSize(), op, Integer.parseInt(val));
            }
        }
        return false;
    }

    private boolean evaluateString(String attribute, String op, String value) {
        value = value.substring(1, value.length() - 1); // Remove the double quotes
        switch (op) {
            case "\"contains\"":
                return attribute.contains(value);
            case "\"equals\"":
                return attribute.equals(value);
            default:
                return false;
        }
    }

    private boolean evaluateInt(int attribute, String op, int value) {
        switch (op) {
            case "\"<\"":
                return attribute < value;
            case "\"<=\"":
                return attribute <= value;
            case "\">\"":
                return attribute > value;
            case "\">=\"":
                return attribute >= value;
            case "\"==\"":
                return attribute == value;
            case "\"!=\"":
                return attribute != value;
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return String.format("SimpleCriterion(attrName: %s, op: %s, val: %s)", attrName, op, val);
    }

    // Getters
    /**
     * @return returns name of criterion
     */
    public String getAttrName() {
        return attrName;
    }
    /**
     * @return returns operator of criterion
     */
    public String getOp() {
        return op;
    }
    /**
     * @return returns value of criterion
     */
    public String getVal() {
        return val;
    }
}

