package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;

/**
 * This class is an abstract base class for all criteria that
 * can be evaluated against a file.
 * Implement Serializable to allow criteria to be saved and loaded
 */
public interface Criterion extends Serializable {
    /**
     * Evaluates the criterion against specified file
     *
     * @param file the file to evaluate
     * @return true: filemeets the criterion ; false: otherwise
     */
    boolean evaluate(Object file);
}