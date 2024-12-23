package hk.edu.polyu.comp.comp2021.cvfs.view;

import hk.edu.polyu.comp.comp2021.cvfs.controller.CommandProcessor;
import hk.edu.polyu.comp.comp2021.cvfs.exceptions.*;

import java.util.Scanner;

/**
 * This class handles the command-line interface
 * for interacting with the CommandProcessor
 */
public class CLIView {
    private CommandProcessor commandProcessor;

    /**
     * Constructs a new CLIView and initializes the CommandProcessor
     */
    public CLIView() {
        setCommandProcessor(new CommandProcessor());
    }

    /**
     * Starts the command-line interface (CLI) for the CommandProcessor
     * Keep reading until the command "quit" is issued
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();
            processCommand(command);
        }
    }

    /**
     * Processes a single command.
     *
     * @param command the command to process
     */
    public void processCommand(String command) {
        String[] parts = command.split(" ");
        try {
            switch (parts[0]) {
                case "newDisk":
                    if (parts.length != 2) {
                        throw new InvalidCommandException("Usage: newDisk diskSize");
                    }
                    getCommandProcessor().newDisk(Integer.parseInt(parts[1]));
                    System.out.println("Disk created with size " + parts[1]);
                    break;
                case "newDoc":
                    if (parts.length != 4) {
                        throw new InvalidCommandException("Usage: newDoc docName docType docContent");
                    }
                    getCommandProcessor().newDoc(parts[1], parts[2], parts[3]);
                    System.out.println("Document " + parts[1] + " created.");
                    break;
                case "newDir":
                    if (parts.length != 2) {
                        throw new InvalidCommandException("Usage: newDir dirName");
                    }
                    getCommandProcessor().newDir(parts[1]);
                    System.out.println("Directory " + parts[1] + " created.");
                    break;
                case "delete":
                    if (parts.length != 2) {
                        throw new InvalidCommandException("Usage: delete fileName");
                    }
                    getCommandProcessor().delete(parts[1]);
                    System.out.println("File " + parts[1] + " deleted.");
                    break;
                case "rename":
                    if (parts.length != 3) {
                        throw new InvalidCommandException("Usage: rename oldFileName newFileName");
                    }
                    getCommandProcessor().rename(parts[1], parts[2]);
                    System.out.println("File " + parts[1] + " renamed to " + parts[2]);
                    break;
                case "changeDir":
                    if (parts.length != 2) {
                        throw new InvalidCommandException("Usage: changeDir dirName");
                    }
                    getCommandProcessor().changeDir(parts[1]);
                    System.out.println("Changed to directory " + parts[1]);
                    break;
                case "list":
                    if (parts.length != 1) {
                        throw new InvalidCommandException("Usage: list");
                    }
                    getCommandProcessor().list();
                    System.out.println("Listing current directory contents.");
                    break;
                case "rList":
                    if (parts.length != 1) {
                        throw new InvalidCommandException("Usage: rList");
                    }
                    getCommandProcessor().rList();
                    System.out.println("Recursively listing directory contents.");
                    break;
                case "newSimpleCri":
                    if (parts.length != 5) {
                        throw new InvalidCommandException("Usage: newSimpleCri criName attrName op val");
                    }
                    getCommandProcessor().newSimpleCri(parts[1], parts[2], parts[3], parts[4]);
                    System.out.println("Simple criteria " + parts[1] + " created.");
                    break;
                case "isDocument":
                    if (parts.length != 2) {
                        throw new InvalidCommandException("Usage: isDocument fileName");
                    }
                    boolean result = getCommandProcessor().isDocument(parts[1]);
                    System.out.println("Is " + parts[1] + " a document? " + result);
                    break;
                case "newNegation":
                    if (parts.length != 3) {
                        throw new InvalidCommandException("Usage: newNegation criName1 criName2");
                    }
                    getCommandProcessor().newNegation(parts[1], parts[2]);
                    System.out.println("Negation criteria " + parts[2] + " created based on " + parts[1]);
                    break;
                case "newBinaryCri":
                    if (parts.length != 5) {
                        throw new InvalidCommandException("Usage: newBinaryCri criName1 criName3 logicOp criName4");
                    }
                    getCommandProcessor().newBinaryCri(parts[1], parts[2], parts[3], parts[4]);
                    System.out.println("Binary criteria " + parts[1] + " created.");
                    break;
                case "printAllCriteria":
                    if (parts.length != 1) {
                        throw new InvalidCommandException("Usage: printAllCriteria");
                    }
                    getCommandProcessor().printAllCriteria();
                    System.out.println("All criteria printed.");
                    break;
                case "search":
                    if (parts.length != 2) {
                        throw new InvalidCommandException("Usage: search criName");
                    }
                    getCommandProcessor().search(parts[1]);
                    System.out.println("Search completed with criteria " + parts[1]);
                    break;
                case "rSearch":
                    if (parts.length != 2) {
                        throw new InvalidCommandException("Usage: rSearch criName");
                    }
                    getCommandProcessor().rSearch(parts[1]);
                    System.out.println("Recursive search completed with criteria " + parts[1]);
                    break;
                case "save":
                    if (parts.length != 2) {
                        throw new InvalidCommandException("Usage: save path");
                    }
                    getCommandProcessor().save(parts[1]);
                    System.out.println("State saved to path " + parts[1]);
                    break;
                case "load":
                    if (parts.length != 2) {
                        throw new InvalidCommandException("Usage: load path");
                    }
                    getCommandProcessor().load(parts[1]);
                    System.out.println("State loaded from path " + parts[1]);
                    break;
                case "undo":
                    if (parts.length != 1) {
                        throw new InvalidCommandException("Usage: undo");
                    }
                    getCommandProcessor().undo();
                    System.out.println("Undo completed.");
                    break;
                case "redo":
                    if (parts.length != 1) {
                        throw new InvalidCommandException("Usage: redo");
                    }
                    getCommandProcessor().redo();
                    System.out.println("Redo completed.");
                    break;
                case "quit":
                    if (parts.length != 1) {
                        throw new InvalidCommandException("Usage: quit");
                    }
                    getCommandProcessor().quit();
                    System.out.println("Exiting. Goodbye!");
                    return;
                default:
                    System.out.println("Unknown command");
            }
        } catch (InvalidCommandException | InvalidFileNameException | DiskSpaceException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * @return gets CommandProcessor for testing
     */
    public CommandProcessor getCommandProcessor() {
        return commandProcessor;
    }

    /**
     * @param commandProcessor sets for Testing
     */
    public void setCommandProcessor(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }
}
