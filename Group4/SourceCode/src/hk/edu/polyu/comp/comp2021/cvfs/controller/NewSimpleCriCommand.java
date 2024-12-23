package hk.edu.polyu.comp.comp2021.cvfs.controller;

import hk.edu.polyu.comp.comp2021.cvfs.model.*;

import java.util.Map;

/**
 * The NewSimpleCriCommand class implements the Command interface and provides the functionality
 * to add a simple criterion to a set of criteria within a VirtualDisk.
 */
public class NewSimpleCriCommand implements Command {
    private Map criteria;
    private String criName;
    private SimpleCriterion criterion;

    /**
     * Constructs a NewSimpleCriCommand with the specified criteria map, criterion name, and simple criterion.
     *
     * @param criteria the map of criteria
     * @param criName the name of the criterion to be added
     * @param criterion the simple criterion to be added
     */
    public NewSimpleCriCommand(Map criteria, String criName, SimpleCriterion criterion) {
        this.criteria = criteria;
        this.criName = criName;
        this.criterion = criterion;
    }

    /**
     * Executes the command, adding the simple criterion to the criteria map.
     */
    @Override
    public void execute() {
        criteria.put(criName, criterion);
    }

    /**
     * Undoes the command, removing the simple criterion from the criteria map.
     */
    @Override
    public void undo() {
        criteria.remove(criName);
    }
}
