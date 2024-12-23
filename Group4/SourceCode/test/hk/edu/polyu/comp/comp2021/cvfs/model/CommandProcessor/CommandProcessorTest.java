package hk.edu.polyu.comp.comp2021.cvfs.model.CommandProcessor;

import hk.edu.polyu.comp.comp2021.cvfs.controller.Command;
import hk.edu.polyu.comp.comp2021.cvfs.controller.CommandProcessor;
import hk.edu.polyu.comp.comp2021.cvfs.exceptions.DiskSpaceException;
import hk.edu.polyu.comp.comp2021.cvfs.exceptions.InvalidCommandException;
import hk.edu.polyu.comp.comp2021.cvfs.exceptions.InvalidFileNameException;
import hk.edu.polyu.comp.comp2021.cvfs.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class CommandProcessorTest {
    private CommandProcessor commandProcessor;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        commandProcessor = new CommandProcessor();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testNewDisk() throws InvalidCommandException {
        commandProcessor.newDisk(100);
        VirtualDisk disk = commandProcessor.getVirtualDisk();
        assertNotNull(disk);
        assertEquals(100, disk.getMaxSize());
    }

    @Test(expected = InvalidCommandException.class)
    public void testNewDiskInvalidSize() throws InvalidCommandException {
        commandProcessor.newDisk(0);
    }
    @Test
    public void testNewDoc() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(400);
        commandProcessor.newDoc("testDoc", "txt", "This is a test document.");
        Document doc = commandProcessor.getVirtualDisk().getWorkingDirectory().getDocument("testDoc");
        assertNotNull(doc);
        assertEquals("testDoc", doc.getName());
        assertEquals("txt", doc.getType());
        assertEquals("This is a test document.", doc.getContent());

        // Use the list method to verify the document is listed
        commandProcessor.list();
    }

    @Test(expected = InvalidFileNameException.class)
    public void testNewDocInvalidType() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(100);
        commandProcessor.newDoc("testDoc", "exe", "This is a test document.");
    }

    @Test(expected = DiskSpaceException.class)
    public void testNewDocNotEnoughSpace() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(10);
        commandProcessor.newDoc("testDoc", "txt", "This is a test document.");
    }

    @Test
    public void testNewDir() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(100);
        commandProcessor.newDir("testDir");
        Directory dir = commandProcessor.getVirtualDisk().getWorkingDirectory().getDirectory("testDir");
        assertNotNull(dir);
        assertEquals("testDir", dir.getName());
    }

    @Test(expected = InvalidFileNameException.class)
    public void testNewDirInvalidName() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(100);
        commandProcessor.newDir("");
    }

    @Test(expected = DiskSpaceException.class)
    public void testNewDirNotEnoughSpace() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(10);
        commandProcessor.newDir("testDir");
    }

    @Test
    public void testDeleteDocument() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(400);
        commandProcessor.newDoc("testDoc", "txt", "This is a test document.");
        commandProcessor.delete("testDoc");
        Document doc = commandProcessor.getVirtualDisk().getWorkingDirectory().getDocument("testDoc");
        assertNull(doc);
    }

    @Test(expected = InvalidCommandException.class)
    public void testDeleteNonExistentFile() throws InvalidCommandException {
        commandProcessor.newDisk(100);
        commandProcessor.delete("nonExistentFile");
    }

    @Test(expected = InvalidCommandException.class)
    public void testDeleteWithoutDisk() throws InvalidCommandException {
        commandProcessor.delete("testDoc");
    }

    @Test
    public void testRenameDocument() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(400);
        commandProcessor.newDoc("testDoc", "txt", "This is a test document.");
        commandProcessor.rename("testDoc", "renamedDoc");
        Document doc = commandProcessor.getVirtualDisk().getWorkingDirectory().getDocument("renamedDoc");
        assertNotNull(doc);
        assertEquals("renamedDoc", doc.getName());
    }

    @Test(expected = InvalidCommandException.class)
    public void testRenameNonExistentFile() throws InvalidFileNameException, InvalidCommandException {
        commandProcessor.newDisk(100);
        commandProcessor.rename("nonExistentFile", "newName");
    }

    @Test(expected = InvalidCommandException.class)
    public void testRenameWithoutDisk() throws InvalidFileNameException, InvalidCommandException {
        commandProcessor.rename("testDoc", "newName");
    }

    @Test(expected = InvalidFileNameException.class)
    public void testRenameInvalidNewName() throws InvalidFileNameException, InvalidCommandException, DiskSpaceException {
        commandProcessor.newDisk(200);
        commandProcessor.newDoc("testDoc", "txt", "This is a test document.");
        commandProcessor.rename("testDoc", "");
    }

    @Test
    public void testChangeDirToSubdirectory() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(100);
        commandProcessor.newDir("subDir");
        commandProcessor.changeDir("subDir");
        Directory currentDir = commandProcessor.getVirtualDisk().getWorkingDirectory();
        assertEquals("subDir", currentDir.getName());
    }

    @Test
    public void testChangeDirToParentDirectory() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(100);
        commandProcessor.newDir("subDir");
        commandProcessor.changeDir("subDir");
        commandProcessor.changeDir("..");
        Directory currentDir = commandProcessor.getVirtualDisk().getWorkingDirectory();
        assertEquals("root", currentDir.getName());
    }

    @Test(expected = InvalidCommandException.class)
    public void testChangeDirToNonExistentDirectory() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(100);
        commandProcessor.changeDir("nonExistentDir");
    }

    @Test(expected = InvalidCommandException.class)
    public void testChangeDirWithoutDisk() throws InvalidCommandException {
        commandProcessor.changeDir("subDir");
    }

    @Test(expected = InvalidCommandException.class)
    public void testChangeDirToRootFromRoot() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(100);
        commandProcessor.changeDir("..");
    }

    @Test
    public void testList() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(400);
        commandProcessor.newDoc("testDoc", "txt", "This is a test document.");
        commandProcessor.newDir("testDir");
        commandProcessor.list();
        // Verify the output manually or use a custom output stream to capture and assert the output
    }

    @Test(expected = InvalidCommandException.class)
    public void testListWithoutDisk() throws InvalidCommandException {
        commandProcessor.list();
    }

    @Test
    public void testRList() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(1000);
        commandProcessor.newDoc("testDoc", "txt", "This is a test document.");
        commandProcessor.newDir("testDir");
        commandProcessor.changeDir("testDir");
        commandProcessor.newDoc("subDoc", "txt", "This is a sub document.");
        commandProcessor.changeDir("..");
        commandProcessor.rList();
        // Verify the output manually or use a custom output stream to capture and assert the output
    }

    @Test(expected = InvalidCommandException.class)
    public void testRListWithoutDisk() throws InvalidCommandException {
        commandProcessor.rList();
    }

    @Test
    public void testNewSimpleCri() {
        commandProcessor.newSimpleCri("si", "size", "\"<\"", "100"); // Include double quotes around the operation
        SimpleCriterion criterion = (SimpleCriterion) commandProcessor.getCriterion("si");
        assertNotNull(criterion);
        assertEquals("size", criterion.getAttrName()); // Remove extra quotes
        assertEquals("\"<\"", criterion.getOp()); // Include double quotes
        assertEquals("100", criterion.getVal());
    }

    @Test
    public void testNewNegation() {
        commandProcessor.newSimpleCri("si", "size", "\">\"", "100");
        commandProcessor.newNegation("no", "si");
        CompositeCriterion negation = (CompositeCriterion) commandProcessor.getCriterion("no");
        assertNotNull(negation);
        assertEquals("!", negation.getOp());
    }

    @Test
    public void testNewBinaryCri() {
        commandProcessor.newSimpleCri("si", "size", "\">\"", "100"); // Operation with double quotes
        commandProcessor.newSimpleCri("na", "name", "\"contains\"", "\"Ab\""); // Operation and value with double quotes
        commandProcessor.newBinaryCri("sz", "si", "&&", "na");
        CompositeCriterion binaryCriterion = (CompositeCriterion) commandProcessor.getCriterion("sz");
        assertNotNull(binaryCriterion);
        assertEquals("&&", binaryCriterion.getOp());
    }

    @Test
    public void testPrintAllCriteria() {
        commandProcessor.newSimpleCri("si", "size", "\">\"", "100");
        commandProcessor.printAllCriteria();
        String expectedOutput = "si: SimpleCriterion(attrName: size, op: \">\", val: 100)\n";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }
    @Test
    public void testSearch() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException {
        commandProcessor.newDisk(800);
        commandProcessor.newDoc("testDoc1", "txt", "This is a test document.");
        commandProcessor.newDoc("testDoc2", "txt", "Another test document.");
        commandProcessor.newDir("testDir");
        commandProcessor.newSimpleCri("si", "size", "\">\"", "60");
        commandProcessor.search("si");
        String expectedOutput = "testDoc1 txt 88\n" + "testDoc2 txt 84\n" + "Total files: 2\n" + "Total size: 172\n";
        Assert.assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test(expected = InvalidCommandException.class)
    public void testSearchWithoutDisk() throws InvalidCommandException {
        commandProcessor.search("sizeGreaterThan60");
    }

    @Test
    public void testRSearch() throws InvalidCommandException, DiskSpaceException, InvalidFileNameException {
        commandProcessor.newDisk(800); // Assuming this initializes the virtual disk
        commandProcessor.newSimpleCri("na", "name", "\"contains\"", "\"test\"");

        // Create a test documentx
        commandProcessor.newDoc("testDoc", "txt", "This is a test content.");
        commandProcessor.rSearch("na");

        String expectedOutput = "testDoc txt 86\n" +
                "\nTotal files: 1\n" +
                "Total size: 126 bytes";
        assertEquals(normalizeLineSeparators(expectedOutput.trim()), normalizeLineSeparators(outContent.toString().trim()));
    }


    private String normalizeLineSeparators(String input) {
        return input.replace("\r\n", "\n").replace("\r", "\n");
    }

    @Test(expected = InvalidCommandException.class)
    public void testRSearchWithoutDisk() throws InvalidCommandException {
        commandProcessor.rSearch("sizeGreaterThan60");
    }

    @Test
    public void testSave() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException, IOException {
        commandProcessor.newDisk(200);
        commandProcessor.newDoc("testDoc", "txt", "This is a test document.");
        commandProcessor.newSimpleCri("si", "size", "\">\"", "60");
        String path = "testSave.dat";
        commandProcessor.save(path);

        File file = new File(path);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        // Clean up
        file.delete();
    }

    @Test(expected = IOException.class)
    public void testSaveWithoutDisk() throws IOException {
        commandProcessor.save("testSave.dat");
    }

    @Test
    public void testLoad() throws InvalidFileNameException, DiskSpaceException, InvalidCommandException, IOException, ClassNotFoundException {
        commandProcessor.newDisk(200);
        commandProcessor.newDoc("testDoc", "txt", "This is a test document.");
        commandProcessor.newSimpleCri("si", "size", "\">\"", "60");
        String path = "testLoad.dat";
        commandProcessor.save(path);

        CommandProcessor newCommandProcessor = new CommandProcessor();
        newCommandProcessor.load(path);

        assertNotNull(newCommandProcessor.getVirtualDisk());
        assertEquals(200, newCommandProcessor.getVirtualDisk().getMaxSize());
        assertNotNull(newCommandProcessor.getVirtualDisk().getWorkingDirectory().getDocument("testDoc"));
        assertNotNull(newCommandProcessor.getCriterion("si"));

        // Clean up
        File file = new File(path);
        file.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadNonExistentFile() throws IOException, ClassNotFoundException {
        commandProcessor.load("nonExistentFile.dat");
    }

    public PrintStream getOriginalOut() {
        return originalOut;
    }


    private static class MockCommand implements Command {
        private boolean executed = false;

        @Override
        public void execute() {
            executed = true;
        }

        @Override
        public void undo() {
            executed = false;
        }

        public boolean isExecuted() {
            return executed;
        }
    }

    @Test
    public void testExecuteCommand() {
        MockCommand mockCommand = new MockCommand();
        commandProcessor.executeCommand(mockCommand);
        assertTrue(mockCommand.isExecuted());
    }

    @Test
    public void testUndo() {
        MockCommand mockCommand = new MockCommand();
        commandProcessor.executeCommand(mockCommand);
        commandProcessor.undo();
        assertFalse(mockCommand.isExecuted());
    }

    @Test
    public void testRedo() {
        MockCommand mockCommand = new MockCommand();
        commandProcessor.executeCommand(mockCommand);
        commandProcessor.undo();
        commandProcessor.redo();
        assertTrue(mockCommand.isExecuted());
    }
}