package hk.edu.polyu.comp.comp2021.cvfs.exceptions;

/**
 * This class represents an exception that is thrown
 *  * when a command is invalid
 */
public class InvalidCommandException extends Exception {
    /**
     * Constructs a new InvalidCommandException with the specified detail message
     *
     * @param message the detail message
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}