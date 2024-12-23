package hk.edu.polyu.comp.comp2021.cvfs.controller;

import hk.edu.polyu.comp.comp2021.cvfs.model.*;

/**
 * The ChangeDirCommand class implements the Command interface and provides the functionality
 * to change the working directory within a VirtualDisk.
 */
public class ChangeDirCommand implements Command {
    private VirtualDisk virtualDisk;
    private Directory oldDirectory;
    private Directory newDirectory;

    /**
     * Constructs a ChangeDirCommand with the specified virtual disk, old directory, and new directory.
     *
     * @param virtualDisk the virtual disk containing the directories
     * @param oldDirectory the current working directory
     * @param newDirectory the new directory to be set as the working directory
     */
    public ChangeDirCommand(VirtualDisk virtualDisk, Directory oldDirectory, Directory newDirectory) {
        this.virtualDisk = virtualDisk;
        this.oldDirectory = oldDirectory;
        this.newDirectory = newDirectory;
    }

    /**
     * Executes the command, changing the working directory to the new directory.
     */
    @Override
    public void execute() {
        virtualDisk.setWorkingDirectory(newDirectory);
    }

    /**
     * Undoes the command, reverting the working directory to the old directory.
     */
    @Override
    public void undo() {
        virtualDisk.setWorkingDirectory(oldDirectory);
    }
}