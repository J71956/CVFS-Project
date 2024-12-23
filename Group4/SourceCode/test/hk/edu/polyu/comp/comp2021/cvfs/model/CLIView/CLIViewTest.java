package hk.edu.polyu.comp.comp2021.cvfs.view;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class CLIViewTest {

    private CLIView cliView;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        cliView = new CLIView();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    private String normalizeLineSeparators(String input) {
        return input.replace("\r\n", "\n").replace("\r", "\n");
    }

    @Test
    public void testNewDiskCommand() {
        String command = "newDisk 100";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }


    @Test
    public void testNewDocCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Then, create a new document
        String command = "newDoc doc1 txt content";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\nDocument doc1 created.\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testInvalidCommand() {
        String command = "invalidCommand";
        cliView.processCommand(command);

        String expectedOutput = "Unknown command\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testNewDirCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Then, create a new directory
        String command = "newDir dir1";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\nDirectory dir1 created.\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testDeleteCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Create a new document to delete
        cliView.processCommand("newDoc doc1 txt content");

        // Then, delete the document
        String command = "delete doc1";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\nDocument doc1 created.\nFile doc1 deleted.\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testRenameCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Create a new document to rename
        cliView.processCommand("newDoc doc1 txt content");

        // Then, rename the document
        String command = "rename doc1 doc2";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\nDocument doc1 created.\nFile doc1 renamed to doc2\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testChangeDirCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Create a new directory to change into
        cliView.processCommand("newDir dir1");

        // Then, change to the new directory
        String command = "changeDir dir1";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\nDirectory dir1 created.\nChanged to directory dir1\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testListCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Create a new directory to list
        cliView.processCommand("newDir dir1");

        // Then, list the contents of the current directory
        String command = "list";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\n" +
                "Directory dir1 created.\n" +
                "dir1 40\n" +
                "Total files: 1\n" +
                "Total size: 40\n" +
                "Listing current directory contents.\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }
    @Test
    public void testRListCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Create a new directory to list recursively
        cliView.processCommand("newDir dir1");

        // Then, recursively list the contents of the current directory
        String command = "rList";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\n" +
                "Directory dir1 created.\n" +
                "dir1 40\n" +
                "Recursively listing directory contents.\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testNewSimpleCriCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Then, create a new simple criterion
        String command = "newSimpleCri cr name \"contains\" \"doc1\"";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\n" +
                "Simple criteria cr created.\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }


    @Test
    public void testNewNegationCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Create a new simple criterion to negate
        cliView.processCommand("newSimpleCri cr name \"contains\" \"doc1\"");

        // Then, create a new negation criterion
        String command = "newNegation cr ce";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\n" +
                "Simple criteria cr created.\n" +
                "Negation criteria ce created based on cr\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testNewBinaryCriCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Create new simple criteria to combine
        cliView.processCommand("newSimpleCri cr name \"contains\" \"doc1\"");
        cliView.processCommand("newSimpleCri ce size \">\" 10");

        // Then, create a new binary criterion
        String command = "newBinaryCri ca cr AND ce";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\n" +
                "Simple criteria cr created.\n" +
                "Simple criteria ce created.\n" +
                "Binary criteria ca created.\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testPrintAllCriteriaCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Create new criteria
        cliView.processCommand("newSimpleCri cr name \"contains\" \"doc1\"");
        cliView.processCommand("newSimpleCri ce size \">\" 10");

        // Then, print all criteria
        String command = "printAllCriteria";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\n" +
                "Simple criteria cr created.\n" +
                "Simple criteria ce created.\n" +
                "ce: SimpleCriterion(attrName: size, op: \">\", val: 10)\n" +
                "cr: SimpleCriterion(attrName: name, op: \"contains\", val: \"doc1\")\n" +
                "All criteria printed.\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testSearchCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Create a new document
        cliView.processCommand("newDoc doc1 txt content");

        // Create a new simple criterion
        cliView.processCommand("newSimpleCri cr name \"contains\" \"doc1\"");

        // Then, perform a search
        String command = "search cr";
        cliView.processCommand(command);

        // Calculate the expected size of the document
        int expectedSize = 40 + 2 * "content".length();

        String expectedOutput = "Disk created with size 100\n" +
                "Document doc1 created.\n" +
                "Simple criteria cr created.\n" +
                "doc1 txt " + expectedSize + "\n" +
                "Total files: 1\n" +
                "Total size: " + expectedSize + "\n"   +
                "Search completed with criteria cr\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testRSearchCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Create a new document
        cliView.processCommand("newDoc doc1 txt content");

        // Create a new simple criterion
        cliView.processCommand("newSimpleCri cr name \"contains\" \"doc1\"");

        // Then, perform a recursive search
        String command = "rSearch cr";
        cliView.processCommand(command);

        // Calculate expected sizes
        int doc1Size = 40 + "content".length() * 2; // 40 + 7*2 = 54

        String expectedOutput = "Disk created with size 100\n" +
                "Document doc1 created.\n" +
                "Simple criteria cr created.\n" +
                "doc1 txt " + doc1Size + "\n\n" + // Extra line after the file details
                "Total files: 1\n" +
                "Total size: " + doc1Size + " bytes\n" +
                "Recursive search completed with criteria cr\n";
    }
    @Test
    public void testSaveCommand() throws IOException {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Create a temporary file to save the state
        File tempFile = File.createTempFile("cvfs", "tmp");
        tempFile.deleteOnExit();

        // Then, save the state
        String command = "save " + tempFile.getAbsolutePath();
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\n" +
                "State saved to path " + tempFile.getAbsolutePath() + "\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testLoadCommand() throws IOException, ClassNotFoundException {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Create a temporary file to save the state
        File tempFile = File.createTempFile("cvfs", "tmp");
        tempFile.deleteOnExit();

        // Save the state
        cliView.processCommand("save " + tempFile.getAbsolutePath());

        // Clear the output stream
        outContent.reset();

        // Load the state
        String command = "load " + tempFile.getAbsolutePath();
        cliView.processCommand(command);

        String expectedOutput = "State loaded from path " + tempFile.getAbsolutePath() + "\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testUndoCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Then, perform an undo
        String command = "undo";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\n" +
                "Undo completed.\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testRedoCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Then, perform a redo
        String command = "redo";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\n" +
                "Redo completed.\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }

    @Test
    public void testIsDocumentCommand() {
        // First, create a new disk with sufficient space
        cliView.processCommand("newDisk 100");

        // Create a new document
        cliView.processCommand("newDoc doc1 txt content");

        // Then, check if the file is a document
        String command = "isDocument doc1";
        cliView.processCommand(command);

        String expectedOutput = "Disk created with size 100\n" +
                "Document doc1 created.\n" +
                "Document found: doc1\n" +
                "Is doc1 a document? true\n";
        assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(outContent.toString()));
    }
}