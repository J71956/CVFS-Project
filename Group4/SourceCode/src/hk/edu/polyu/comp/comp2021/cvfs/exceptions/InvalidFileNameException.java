package hk.edu.polyu.comp.comp2021.cvfs.exceptions;

/**
 * This class represents an exception that is thrown
 * when a file name is invalid with specified criteria
 */

public class InvalidFileNameException extends Exception {
    /**
     * Constructs a new InvalidFileNameException with the specified detail message
     *
     * @param message the detail message
     */
    public InvalidFileNameException(String message) {
        super(message);
    }
}
