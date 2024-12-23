package hk.edu.polyu.comp.comp2021.cvfs.controller;

/**
 *  The Command interface defines the contract for executing and undoing operations
 */
public interface Command {
    /**
     * Executes the command
     */
    void execute();

    /**
     * Undoes the command
     */
    void undo();
}
