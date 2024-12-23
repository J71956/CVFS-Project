package hk.edu.polyu.comp.comp2021.cvfs.model;

/**
 * This class represents a compound criterion that combines
 * two existing criteria using a logical operator
 */
public class CompositeCriterion implements Criterion {
    private Criterion cri1;
    private Criterion cri2;
    private String logicOp;

    /**
     * Constructs a new CompositeCriterion with specified criteria and logical operator
     *
     * @param cri1 the first criterion
     * @param cri2 the second criterion
     * @param logicOp the logical operator to combine the criteria
     */
    public CompositeCriterion(Criterion cri1, Criterion cri2, String logicOp) {
        this.cri1 = cri1;
        this.cri2 = cri2;
        this.logicOp = logicOp;
    }

    @Override
    public boolean evaluate(Object file) {
        switch (logicOp) {
            case "&&":
                return cri1.evaluate(file) && cri2.evaluate(file);
            case "||":
                return cri1.evaluate(file) || cri2.evaluate(file);
            case "!":
                return !cri1.evaluate(file);
            default:
                return false;
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(cri1.toString()).append(" ").append(logicOp).append(" ");
        if (cri2 != null) {
            sb.append(cri2);
        }
        sb.append(")");
        return sb.toString();
    }
    // Getters

    /**
     * @return logic operator
     */
    public String getOp() {
        return logicOp;
    }
}
