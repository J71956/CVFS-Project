package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;
/**
 * Represents a virtual disk in a file system which includes methods to manage directions and check disk space
 */
public class VirtualDisk implements Serializable {
    private int maxSize;
    private Directory rootDirectory;
    private Directory workingDirectory;

    /**
     * Constructs a new virtual disk with specified size
     *
     * @param maxSize maximum size of the virtual disk
     */
    public VirtualDisk(int maxSize) {
        this.maxSize = maxSize;
        this.rootDirectory = new Directory("root");
        this.workingDirectory = rootDirectory;
    }

    /**
     * @return the root directory of the virtual disk
     */
    public Directory getRootDirectory() {
        return rootDirectory;
    }

    /**
     * @return the current working directory of the virtual disk
     */
    public Directory getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * @param workingDirectory the new working directory
     */
    public void setWorkingDirectory(Directory workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    /**
     * @return the maximum size of the virtual disk
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * @return the used size of the virtual disk
     */
    public int getUsedSize() {
        return rootDirectory.getSize();
    }

    /**
     * Chccks if there is enough space for a specified size
     * @param size the size for checking
     * @return true: enough space ; false: otherwise
     */
    public boolean hasEnoughSpace(int size) {
        return getUsedSize() + size <= maxSize;
    }
}