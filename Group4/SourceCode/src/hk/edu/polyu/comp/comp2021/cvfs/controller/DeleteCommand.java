package hk.edu.polyu.comp.comp2021.cvfs.controller;

import hk.edu.polyu.comp.comp2021.cvfs.model.*;

/**
 * The DeleteCommand class implements the Command interface and provides the functionality
 * to delete a document from the current working directory within a VirtualDisk.
 */
public class DeleteCommand implements Command {
    private VirtualDisk virtualDisk;
    private Document document;
    private Directory workingDir;

    /**
     * Constructs a DeleteCommand with the specified virtual disk and document.
     *
     * @param virtualDisk the virtual disk containing the document
     * @param document the document to be deleted
     */
    public DeleteCommand(VirtualDisk virtualDisk, Document document) {
        this.virtualDisk = virtualDisk;
        this.document = document;
        this.workingDir = virtualDisk.getWorkingDirectory();
    }

    /**
     * Executes the delete command, removing the document from the working directory.
     */
    @Override
    public void execute() {
        workingDir.removeFile(document);
    }

    /**
     * Undoes the delete command, adding the document back to the working directory.
     */
    @Override
    public void undo() {
        workingDir.addFile(document);
    }
}