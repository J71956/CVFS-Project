package hk.edu.polyu.comp.comp2021.cvfs.exceptions;

/**
 * THis class represents an exception that is thrown
 * when there is not enough disk sapce
 */
public class DiskSpaceException extends Exception {
    /**
     *  Constructs a new DiskSpaceExeception with specified detail message
     *
     * @param message the detail message
     */
    public DiskSpaceException(String message) {
        super(message);
    }
}
