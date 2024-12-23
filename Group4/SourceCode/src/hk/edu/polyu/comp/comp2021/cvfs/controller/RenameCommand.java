package hk.edu.polyu.comp.comp2021.cvfs.controller;

import hk.edu.polyu.comp.comp2021.cvfs.model.*;

/**
 * The RenameCommand class implements the Command interface and provides the functionality
 * to rename a file (either a Document or a Directory) within a VirtualDisk.
 */
public class RenameCommand implements Command {
    private VirtualDisk virtualDisk;
    private Object file;
    private String oldFileName;
    private String newFileName;

    /**
     * Constructs a RenameCommand with the specified virtual disk, file, old file name, and new file name.
     *
     * @param virtualDisk the virtual disk containing the file
     * @param file the file to be renamed
     * @param oldFileName the current name of the file
     * @param newFileName the new name for the file
     */
    public RenameCommand(VirtualDisk virtualDisk, Object file, String oldFileName, String newFileName) {
        this.virtualDisk = virtualDisk;
        this.file = file;
        this.oldFileName = oldFileName;
        this.newFileName = newFileName;
    }

    /**
     * Executes the rename command, changing the file's name to the new name.
     */
    @Override
    public void execute() {
        if (file instanceof Document) {
            ((Document) file).setName(newFileName);
        } else if (file instanceof Directory) {
            ((Directory) file).setName(newFileName);
        }
    }

    /**
     * Undoes the rename command, reverting the file's name to the old name.
     */
    @Override
    public void undo() {
        if (file instanceof Document) {
            ((Document) file).setName(oldFileName);
        } else if (file instanceof Directory) {
            ((Directory) file).setName(oldFileName);
        }
    }
}
