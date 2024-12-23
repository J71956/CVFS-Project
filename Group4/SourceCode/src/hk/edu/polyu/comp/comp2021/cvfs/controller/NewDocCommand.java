package hk.edu.polyu.comp.comp2021.cvfs.controller;

import hk.edu.polyu.comp.comp2021.cvfs.model.*;

/**
 * Creating a new document on the virtual disk
 */
public class NewDocCommand implements Command {
    private VirtualDisk virtualDisk;
    private Document document;

    /**
     * Constructs a new command to create a document on the speficied virtual disk
     *
     * @param virtualDisk the virtual disk where the document will be created
     * @param document the document to be created
     */
    public NewDocCommand(VirtualDisk virtualDisk, Document document) {
        this.virtualDisk = virtualDisk;
        this.document = document;
    }

    @Override
    public void execute() {
        virtualDisk.getWorkingDirectory().addFile(document);
    }

    @Override
    public void undo() {
        virtualDisk.getWorkingDirectory().removeFile(document);
    }
}