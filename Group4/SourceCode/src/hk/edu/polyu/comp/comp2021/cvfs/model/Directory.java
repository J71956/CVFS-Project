package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  Represents a directory a virtual file system
 *  It can contain both documents and other directories
 */
public class Directory implements Serializable {
    /**
     * Initialise Directory variables
     */
    public static final int SIZE = 40;
    private String name;
    private List<Object> files;

    /**
     * Constructs a new directory with specified name
     *
     * @param name name of the directory
     */
    public Directory(String name) {
        this.name = name;
        this.files = new ArrayList<>();
    }

    /**
     * @return the name of the directory
     */
    public String getName() {
        return name;
    }

    /**
     * @return the list of files in the directory
     */
    public List<Object> getFiles() {
        return files;
    }

    /**
     * Adds a file to the directory
     *
     * @param file the file to be added
     */
    public void addFile(Object file) {
        files.add(file);
    }

    /**
     * Removes a file from the directory
     *
     * @param file the file to be removed
     */
    public void removeFile(Object file) {
        files.remove(file);
    }

    /**
     * @return the size of the directory
     */
    public int getSize() {
        int size = SIZE;
        for (Object file : files) {
            if (file instanceof Document) {
                size += ((Document) file).getSize(); // Use Document's getSize()
            } else if (file instanceof Directory) {
                size += SIZE + ((Directory) file).getSize(); // Use Directory's getSize() + 40
            }
        }
        return size;
    }

    /**
     * @param name name of the document
     * @return returns document name
     */
    public Document getDocument(String name) {
        for (Object file : files) {
            if (file instanceof Document) {
                Document doc = (Document) file;
                if (doc.getName().equals(name)) {
                    return doc;
                }
            }
        }
        return null;
    }
    /**
     * @param name name of the Directory
     * @return returns Directory name
     */
    public Directory getDirectory(String name) {
        for (Object file : files) {
            if (file instanceof Directory) {
                Directory dir = (Directory) file;
                if (dir.getName().equals(name)) {
                    return dir;
                }
            }
        }
        return null;
    }


    /**
     * Sets the name of the directory
     *
     * @param name the new name of the directory
     */
    public void setName(String name) {
        this.name = name;
    }
}




