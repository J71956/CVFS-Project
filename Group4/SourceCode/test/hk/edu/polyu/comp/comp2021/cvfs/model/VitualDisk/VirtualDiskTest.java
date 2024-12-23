package hk.edu.polyu.comp.comp2021.cvfs.model.VitualDisk;
import hk.edu.polyu.comp.comp2021.cvfs.exceptions.*;
import static org.junit.Assert.*;

import hk.edu.polyu.comp.comp2021.cvfs.model.Directory;
import hk.edu.polyu.comp.comp2021.cvfs.model.VirtualDisk;
import org.junit.Before;
import org.junit.Test;

public class VirtualDiskTest {

    private VirtualDisk virtualDisk;

    @Before
    public void setUp() {
        // Initialize the virtual disk with a max size of 1000 for testing
        virtualDisk = new VirtualDisk(1000);
    }

    /**
     * Test creating a VirtualDisk with valid size.
     */
    @Test
    public void testValidInitialization() {
        VirtualDisk disk = new VirtualDisk(1000);
        assertNotNull(disk);
        assertEquals(1000, disk.getMaxSize());
    }

    /**
     * Test creating a VirtualDisk with invalid size.
     */
    @Test //(expected = InvalidCommandException.class)
    public void testInvalidCommand() throws InvalidCommandException {
        // Assuming you have a method that throws InvalidCommandException


}

    /**
     * Test hasEnoughSpace method with various file sizes.
     */
    @Test
    public void testHasEnoughSpace() {
        assertTrue(virtualDisk.hasEnoughSpace(500)); // Expected to be true
        assertFalse(virtualDisk.hasEnoughSpace(1000)); // Expected to be true
        assertFalse(virtualDisk.hasEnoughSpace(1001)); // Expected to be false
    }

    /**
     * Ensure the root directory is correctly initialized.
     */
    @Test
    public void testRootDirectoryInitialization() {
        assertNotNull(virtualDisk.getRootDirectory());
        assertEquals("root", virtualDisk.getRootDirectory().getName());
    }

    /**
     * Ensure the working directory can be set and retrieved.
     */
    @Test
    public void testSetAndGetWorkingDirectory() {
        Directory newDir = new Directory("newDir");
        virtualDisk.setWorkingDirectory(newDir);
        assertEquals(newDir, virtualDisk.getWorkingDirectory());
    }

    /**
     * Test getUsedSize method.
     */
    @Test
    public void testGetUsedSize() {
        // Assume the root directory's size starts at 40
        assertEquals(Directory.SIZE, virtualDisk.getUsedSize());
    }
}

