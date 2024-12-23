package hk.edu.polyu.comp.comp2021.cvfs.controller;

import hk.edu.polyu.comp.comp2021.cvfs.model.*;

/**
 * The class is responsible for creating a new directory on the vitual dik
 */
public class NewDirCommand implements Command {
    private VirtualDisk virtualDisk;
    private Directory directory;

    /**
     *  Constructs a new command to create a dicrectory on the specified virtual disk
     *
     * @param virtualDisk the virtual disk where the directory will be created
     * @param directory the directory to be created
     */
    public NewDirCommand(VirtualDisk virtualDisk, Directory directory) {
        this.virtualDisk = virtualDisk;
        this.directory = directory;
    }

    @Override
    public void execute() {
        virtualDisk.getWorkingDirectory().addFile(directory);
    }

    @Override
    public void undo() {
        virtualDisk.getWorkingDirectory().removeFile(directory);
    }
}
