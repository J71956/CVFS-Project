package hk.edu.polyu.comp.comp2021.cvfs.controller;

import hk.edu.polyu.comp.comp2021.cvfs.model.*;
import hk.edu.polyu.comp.comp2021.cvfs.exceptions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 *  The CommandProcessor class handles the execution
 *  and management of commands * related to a virtual disk system
 */
public class CommandProcessor {
    private VirtualDisk virtualDisk;
    private Map<String, Criterion> criteria;
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;
    private Directory parentDirectory;

    /**
     * Constructs a new CommandProcessor with empty criteria
     * and stacks for undo and redo operations
     */
    public CommandProcessor() {
        criteria = new HashMap<>();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }


    /**
     * Creates a new virtual disk with the specificed size
     *
     * @param diskSize the size of the new disk
     * @throws InvalidCommandException if disk size <= 0
     */
    public void newDisk(int diskSize) throws InvalidCommandException {
        if (diskSize <= 0) {
            throw new InvalidCommandException("Disk size must be greater than zero.");
        }
        virtualDisk = new VirtualDisk(diskSize);
        parentDirectory = new Directory("root");
    }

    /**
     * Retrieves the current virtual disk
     *
     * @return the current virtual disk
     */
    public VirtualDisk getVirtualDisk() {
        return virtualDisk;
    }

    /**
     * Creates a new document with specified name , type and content
     *
     * @param docName name of the document
     * @param docType type of the document
     * @param docContent the content of the document
     * @throws InvalidFileNameException if name or type is invalid
     * @throws DiskSpaceException if there is not enough space
     */
    public void newDoc(String docName, String docType, String docContent) throws InvalidFileNameException, DiskSpaceException {
        validateFileName(docName);
        if (!docType.matches("txt|java|html|css")) {
            throw new InvalidFileNameException("Invalid document type. Allowed types are txt, java, html, css.");
        }
        int docSize = Document.size + docContent.length() * 2;
        if (virtualDisk != null && virtualDisk.hasEnoughSpace(docSize)) {
            Document doc = new Document(docName, docType, docContent);
            NewDocCommand command = new NewDocCommand(virtualDisk, doc);
            command.execute();
            undoStack.push(command);
            redoStack.clear(); // Clear the redo stack whenever a new command is executed
        } else {
            throw new DiskSpaceException("Not enough space on the disk.");
        }
    }



    /**
     *  Creates a new dicectory
     *
     * @param dirName name of the directory
     * @throws InvalidFileNameException if directory name is invalid
     * @throws DiskSpaceException if not enough space on the disk
     */
    public void newDir(String dirName) throws InvalidFileNameException, DiskSpaceException {
        validateFileName(dirName);
        if (virtualDisk != null && virtualDisk.hasEnoughSpace(Document.size)) {
            Directory dir = new Directory(dirName);
            NewDirCommand command = new NewDirCommand(virtualDisk, dir);
            command.execute();
            undoStack.push(command);
            redoStack.clear(); // Clear the redo stack whenever a new command is executed
        } else {
            throw new DiskSpaceException("Not enough space on the disk.");
        }
    }

    private void validateFileName(String fileName) throws InvalidFileNameException {
        if (!fileName.matches("[a-zA-Z0-9]{1,10}")) {
            throw new InvalidFileNameException("Invalid file name. Only digits and English letters are allowed, and the name must be at most 10 characters long.");
        }
    }

    /**
     *  Deletes the file or directory
     *
     * @param fileName name of the file to be deleted
     * @throws InvalidCommandException if no disk is loaded / file not found
     */
    public void delete(String fileName) throws InvalidCommandException {
        if (virtualDisk != null) {
            Directory workingDir = virtualDisk.getWorkingDirectory();
            Object fileToDelete = null;
            for (Object obj : workingDir.getFiles()) {
                if (obj instanceof Document && ((Document) obj).getName().equals(fileName)) {
                    fileToDelete = obj;
                    break;
                } else if (obj instanceof Directory && ((Directory) obj).getName().equals(fileName)) {
                    fileToDelete = obj;
                    break;
                }
            }
            if (fileToDelete != null) {
                DeleteCommand command = new DeleteCommand(virtualDisk, (Document) fileToDelete);
                command.execute();
                undoStack.push(command);
                redoStack.clear(); // Clear the redo stack whenever a new command is executed
            } else {
                throw new InvalidCommandException("File not found: " + fileName);
            }
        } else {
            throw new InvalidCommandException("No virtual disk loaded.");
        }
    }

    /**
     *  Renames the file with a new name
     *
     * @param oldFileName old name of the file
     * @param newFileName new desired name for the file
     * @throws InvalidFileNameException if the new name is invalid
     * @throws InvalidCommandException if no virtual disk loaded / file not found
     */
    public void rename(String oldFileName, String newFileName) throws InvalidFileNameException, InvalidCommandException {
        validateFileName(newFileName);
        if (virtualDisk != null) {
            Directory workingDir = virtualDisk.getWorkingDirectory();
            Object fileToRename = null;
            for (Object file : workingDir.getFiles()) {
                if (file instanceof Document && ((Document) file).getName().equals(oldFileName)) {
                    fileToRename = file;
                    break;
                } else if (file instanceof Directory && ((Directory) file).getName().equals(oldFileName)) {
                    fileToRename = file;
                    break;
                }
            }
            if (fileToRename != null) {
                RenameCommand command = new RenameCommand(virtualDisk, fileToRename, oldFileName, newFileName);
                command.execute();
                undoStack.push(command);
                redoStack.clear(); // Clear the redo stack whenever a new command is executed
            } else {
                throw new InvalidCommandException("File not found: " + oldFileName);
            }
        } else {
            throw new InvalidCommandException("No virtual disk loaded.");
        }
    }

    /**
     *  changes the courrent working directory
     *
     * @param dirName the name of the directory for changing
     * @throws InvalidCommandException if no disk loaded / directory not found / alreaay exists
     */
    public void changeDir(String dirName) throws InvalidCommandException {
        if (virtualDisk != null) {
            Directory currentDirectory = virtualDisk.getWorkingDirectory();
            Directory targetDirectory = getTargetDirectory(dirName, currentDirectory);

            if (targetDirectory != null) {
                ChangeDirCommand command = new ChangeDirCommand(virtualDisk, currentDirectory, targetDirectory);
                command.execute();
                undoStack.push(command);
                redoStack.clear(); // Clear the redo stack whenever a new command is executed
            }
        } else {
            throw new InvalidCommandException("No virtual disk loaded.");
        }
    }

    private Directory getTargetDirectory(String dirName, Directory currentDirectory) throws InvalidCommandException {
        Directory targetDirectory = null;

        if (dirName.equals("..")) {
            if (currentDirectory != virtualDisk.getRootDirectory()) {
                targetDirectory = parentDirectory;
            } else {
                throw new InvalidCommandException("Already at the root directory.");
            }
        } else {
            for (Object file : currentDirectory.getFiles()) {
                if (file instanceof Directory && ((Directory) file).getName().equals(dirName)) {
                    targetDirectory = (Directory) file;
                    break;
                }
            }
            if (targetDirectory == null) {
                throw new InvalidCommandException("Directory not found: " + dirName);
            }
        }
        return targetDirectory;
    }

    /**
     * List all files and directories in current working directory
     * @throws InvalidCommandException If no disk has been created or loaded, this happens
     */
    public void list() throws InvalidCommandException {
        if (virtualDisk == null) {
            throw new InvalidCommandException("Virtual disk has not been initialized. Please call newDisk() first.");
        }

        Directory workingDir = virtualDisk.getWorkingDirectory();
        int totalSize = 0;
        int fileCount = 0;
        Set<Object> processedFiles = new HashSet<>(); // To keep track of processed files

        for (Object file : workingDir.getFiles()) {
            if (!processedFiles.contains(file)) {
                processedFiles.add(file); // Mark the file as processed
                if (file instanceof Document) {
                    Document doc = (Document) file;
                    System.out.println(doc.getName() + " " + doc.getType() + " " + doc.getSize());
                    totalSize += doc.getSize();
                } else if (file instanceof Directory) {
                    Directory dir = (Directory) file;
                    System.out.println(dir.getName() + " " + dir.getSize());
                    totalSize += dir.getSize();
                }
                fileCount++;
            }
        }
        System.out.println("Total files: " + fileCount);
        System.out.println("Total size: " + totalSize);
    }

    /**
     * Lists all files and directories in current working directory
     *  and subdirectories recursively
     * @throws InvalidCommandException If no disk has been created or loaded, this happens
     */
    public void rList() throws InvalidCommandException {
        if (virtualDisk == null) {
            throw new InvalidCommandException("Virtual disk has not been initialized. Please call newDisk() first.");
        }
        Set<Object> processedFiles = new HashSet<>(); // To keep track of processed files
        recursiveList(virtualDisk.getWorkingDirectory(), 0, processedFiles);
    }

    private void recursiveList(Directory dir, int level, Set<Object> processedFiles) {
        if (dir == null || dir.getFiles() == null) {
            return;
        }
        for (Object file : dir.getFiles()) {
            if (!processedFiles.contains(file)) {
                processedFiles.add(file); // Mark the file as processed
                if (file instanceof Document) {
                    Document doc = (Document) file;
                    System.out.println(repeat(level) + doc.getName() + " " + doc.getType() + " " + doc.getSize());
                } else if (file instanceof Directory) {
                    Directory subDir = (Directory) file;
                    System.out.println(repeat(level) + subDir.getName() + " " + subDir.getSize());
                    recursiveList(subDir, level + 1, processedFiles);
                }
            }
        }
    }

    private String repeat(int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }


    /**
     *  Creates a new simple criterion with the specificed attributes
     *
     * @param criName name of the criterion
     * @param attrName attribute name to evaluate
     * @param op the operation to apply
     * @param val the value to compare against
     */
    public void newSimpleCri(String criName, String attrName, String op, String val) {
        if (criName == null || criName.length() != 2 || !criName.matches("[a-zA-Z]{2}")) {
            throw new IllegalArgumentException("Criterion name must be exactly 2 English characters.");
        }
        if (attrName == null || !(attrName.equals("name") || attrName.equals("type") || attrName.equals("size"))) {
            throw new IllegalArgumentException("Attribute name must be either 'name', 'type', or 'size'.");
        }
        switch (attrName) {
            case "name":
                if (!op.equals("\"contains\"") || !val.matches("\"[^\"]*\"")) {
                    throw new IllegalArgumentException("For 'name', operation must be '\"contains\"' and value must be a string in double quotes.");
                }
                break;
            case "type":
                if (!op.equals("\"equals\"") || !val.matches("\"[^\"]*\"")) {
                    throw new IllegalArgumentException("For 'type', operation must be '\"equals\"' and value must be a string in double quotes.");
                }
                break;
            case "size":
                if (!op.matches("\"[<>=!]=?\"") || !val.matches("\\d+")) {
                    throw new IllegalArgumentException("For 'size', operation must be one of '\"<\"', '\"<=\"', '\"==\"', '\"!=\"', '\"<=\"' and value must be an integer.");
                }
                break;
        }
        SimpleCriterion criterion = new SimpleCriterion(attrName, op, val);
        NewSimpleCriCommand command = new NewSimpleCriCommand(criteria, criName, criterion);
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Clear the redo stack whenever a new command is executed
    }

    /**
     * Checks if a file is a document based on its type
     *
     * @param fileName the name of the file
     * @return true if the file is a document, false otherwise
     * @throws InvalidCommandException When no disk is loaded
     */
    public boolean isDocument(String fileName) throws InvalidCommandException {
        if (virtualDisk == null) {
            throw new InvalidCommandException("Virtual disk has not been initialized. Please call newDisk() first.");
        }
        Document doc = findDocumentInDirectory(virtualDisk.getWorkingDirectory(), fileName);
        if (doc != null) {
            System.out.println("Document found: " + doc.getName());
            return true;
        } else {
            System.out.println("Document not found: " + fileName);
        }
        return false;
    }


    /**
     * Recursively searches for a document in the directory and its subdirectories
     *
     * @param directory the directory to search in
     * @param fileName the name of the file
     * @return the document if found, null otherwise
     */
    private Document findDocumentInDirectory(Directory directory, String fileName) {
        for (Object file : directory.getFiles()) {
            if (file instanceof Document) {
                Document doc = (Document) file;
                if (doc.getName().equals(fileName)) {
                    return doc;
                }
            } else if (file instanceof Directory) {
                Document doc = findDocumentInDirectory((Directory) file, fileName);
                if (doc != null) {
                    return doc;
                }
            }
        }
        return null;
    }
    /**
     * @param criName1 the name of the new negation criterion
     * @param criName2 the name of the existing criterion to negate
     */
    public void newNegation(String criName1, String criName2) {
        Criterion criterion = criteria.get(criName2);
        if (criterion == null) {
            System.err.println("Error: Criterion '" + criName2 + "' not found.");
            return;
        }
        CompositeCriterion negation = new CompositeCriterion(criterion, null, "!");
        NewNegationCommand command = new NewNegationCommand(criteria, criName1, criName2, negation);
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Clear the redo stack whenever a new command is executed
    }

    /**
     *  Creates a new binary criterion combinging two existin criteria
     *  with a logical operator
     *
     * @param criName1 name of the new binary criterion
     * @param criName3 name of the first existing criterion
     * @param logicOp logical operator to combine the criteria
     * @param criName4 name of the second existing criterion
     */
    public void newBinaryCri(String criName1, String criName3, String logicOp, String criName4) {
        Criterion criterion1 = criteria.get(criName3);
        Criterion criterion2 = criteria.get(criName4);
        if (criterion1 == null) {
            System.err.println("Error: Criterion '" + criName3 + "' not found.");
        }
        if (criterion2 == null) {
            System.err.println("Error: Criterion '" + criName4 + "' not found.");
        }
        if (criterion1 != null && criterion2 != null) {
            CompositeCriterion binaryCriterion = new CompositeCriterion(criterion1, criterion2, logicOp);
            criteria.put(criName1, binaryCriterion);
        }
    }

    /**
     * @param criName Name of the Criterion
     * @return gets the Criterion name
     */
    public Criterion getCriterion(String criName) {
        return criteria.get(criName);
    }

    /**
     * print all the crrterias in the entry set
     */
    public void printAllCriteria() {
        if (criteria.isEmpty()) {
            System.out.println("There are no criteria loaded.");
        } else {
            for (Map.Entry entry : criteria.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    /**
     *  Searches for files in current working directory
     *  which match the criteria
     *
     * @param criName name of the criterion to use for the search
     * @throws InvalidCommandException If no disk has been created or loaded, this happens
     */
    public void search(String criName) throws InvalidCommandException {
        if (virtualDisk == null) {
            throw new InvalidCommandException("Virtual disk has not been initialized. Please call newDisk() first.");
        }
        Criterion criterion = criteria.get(criName);
        if (criterion != null) {
            Directory workingDir = virtualDisk.getWorkingDirectory();
            int totalSize = 0;
            int fileCount = 0;
            StringBuilder output = new StringBuilder();
            Set<Object> processedFiles = new HashSet<>(); // To keep track of processed files

            for (Object file : workingDir.getFiles()) {
                if (criterion.evaluate(file) && !processedFiles.contains(file)) {
                    processedFiles.add(file); // Mark the file as processed
                    if (file instanceof Document) {
                        Document doc = (Document) file;
                        output.append(doc.getName()).append(" ").append(doc.getType()).append(" ").append(doc.getSize()).append("\n");
                        totalSize += doc.getSize();
                    } else if (file instanceof Directory) {
                        Directory dir = (Directory) file;
                        output.append(dir.getName()).append(" ").append(dir.getSize()).append("\n");
                        totalSize += dir.getSize();
                    }
                    fileCount++;
                }
            }
            output.append("Total files: ").append(fileCount).append("\n");
            output.append("Total size: ").append(totalSize);
            System.out.println(output);
        }
    }

    /**
     *  Recursively searches for files in the current working directory
     *  and its subdirectories that match the criterion
     *
     * @param criName name of the criterion to use for the recursive search
     * @throws InvalidCommandException If no disk has been created or loaded, this happens
     */
    public void rSearch(String criName) throws InvalidCommandException {
        if (virtualDisk == null) {
            throw new InvalidCommandException("Virtual disk has not been initialized. Please call newDisk() first.");
        }
        Criterion criterion = criteria.get(criName);
        if (criterion != null) {
            StringBuilder output = new StringBuilder();
            Set<Object> processedFiles = new HashSet<>(); // To keep track of processed files
            int[] fileCount = {0}; // Array to hold the count of files
            long[] totalSize = {0}; // Array to hold the total size of files
            recursiveSearch(virtualDisk.getWorkingDirectory(), criterion, 0, output, processedFiles, fileCount, totalSize);
            System.out.println(output);
            System.out.println("Total files: " + fileCount[0]);
            System.out.println("Total size: " + totalSize[0] + " bytes");
        }
    }

    private void recursiveSearch(Directory dir, Criterion criterion, int level, StringBuilder output, Set<Object> processedFiles, int[] fileCount, long[] totalSize) {
        long dirSize = Directory.SIZE; // Base size of the director
        for (Object file : dir.getFiles()) {
            if (criterion.evaluate(file) && !processedFiles.contains(file)) {
                processedFiles.add(file); // Mark the file as processed
                appendFileInfo(file, level, output);
                fileCount[0]++; // Increment the file count
                if (file instanceof Document) {
                    Document doc = (Document) file;
                    long docSize = doc.getSize();
                    totalSize[0] += docSize; // Add the size of the document to the total size
                } else if (file instanceof Directory) {
                    recursiveSearch((Directory) file, criterion, level + 1, output, processedFiles, fileCount, totalSize);
                }
            }
        }
        totalSize[0] += dirSize; // Add the size of the directory to the total size
    }

    private void appendFileInfo(Object file, int level, StringBuilder output) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level * 2; i++) {
            indent.append(" ");
        }
        if (file instanceof Document) {
            Document doc = (Document) file;
            output.append(indent).append(doc.getName()).append(" ").append(doc.getType()).append(" ").append(doc.getSize()).append("\n");
        } else if (file instanceof Directory) {
            Directory dir = (Directory) file;
            output.append(indent).append(dir.getName()).append(" ").append(Directory.SIZE).append("\n");
        }
    }
    /**
     *  Saves the current state of the virtual disk and criteria to the specified path
     *
     * @param path the path to save the virtual disk
     * @throws IOException if I/O error occurs during saving
     */
    public void save(String path) throws IOException {
        if (virtualDisk != null) {
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(path)))) {
                oos.writeObject(virtualDisk);
                oos.writeObject(criteria);
            }
        } else {
            throw new IOException("No virtual disk loaded.");
        }
    }

    /**
     *  Loads the state of the virtual disk and criteria from the specified path
     *
     * @param path the path to load the virtual disk and criteria
     * @throws IOException if an I/O error occurs during loading
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    @SuppressWarnings("unchecked")
    public void load(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(path)))) {
            virtualDisk = (VirtualDisk) ois.readObject();
            criteria = (Map<String, Criterion>) ois.readObject();
        }
    }

    /**
     * @param command command from the CLIView
     */
    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    /**
     * Undoes the last executed command
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    /**
     * Redoes the last undones command
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }


    /**
     * Exixts the application
     */
    public void quit() {
        System.exit(0);
    }


}
