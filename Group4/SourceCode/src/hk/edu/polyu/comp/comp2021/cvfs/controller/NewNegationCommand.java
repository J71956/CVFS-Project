package hk.edu.polyu.comp.comp2021.cvfs.controller;

import hk.edu.polyu.comp.comp2021.cvfs.model.*;

import java.util.Map;

/**
 * The NewNegationCommand class implements the Command interface and provides the functionality
 * to add a negation criterion to a set of criteria within a VirtualDisk.
 */
public class NewNegationCommand implements Command {
    private Map criteria;
    private String criName1;
    private String criName2;
    private CompositeCriterion negation;

    /**
     * Constructs a NewNegationCommand with the specified criteria map, criterion names, and negation criterion.
     *
     * @param criteria the map of criteria
     * @param criName1 the name of the criterion to be negated
     * @param criName2 the name of the second criterion (if applicable)
     * @param negation the negation criterion to be added
     */
    public NewNegationCommand(Map criteria, String criName1, String criName2, CompositeCriterion negation) {
        this.criteria = criteria;
        this.criName1 = criName1;
        this.criName2 = criName2;
        this.negation = negation;
    }

    /**
     * Executes the command, adding the negation criterion to the criteria map.
     */
    @Override
    public void execute() {
        criteria.put(criName1, negation);
    }

    /**
     * Undoes the command, removing the negation criterion from the criteria map.
     */
    @Override
    public void undo() {
        criteria.remove(criName1);
    }
}
