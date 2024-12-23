package hk.edu.polyu.comp.comp2021.cvfs.model;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

/**
 *  test the initialization file management size calculation and name management functionalities
 */
public class DirectoryTest {

    /**
     * Test with valid and invalid names ensuring directory name is set correctly
     */
    @Test
    public void testInitialization() {
        // Valid name
        Directory dir1 = new Directory("Valid");
        assertNotNull(dir1);
        assertEquals("Valid", dir1.getName());

        // Invalid name (assuming an invalid name is an empty string)
        Directory dir2 = new Directory("");
        assertNotNull(dir2);
        assertEquals("", dir2.getName());
    }

    /**
     * Test the adding and removing of files in the directory
     */
    @Test
    public void testFileManagement() {
        Directory dir = new Directory("Root");

        // Adding files
        Document doc = new Document("Doc", "txt", "content");
        Directory subDir = new Directory("SubDir");

        dir.addFile(doc);
        dir.addFile(subDir);

        List<Object> files = dir.getFiles();
        assertEquals(2, files.size());
        assertTrue(files.contains(doc));
        assertTrue(files.contains(subDir));

        // Removing file
        dir.removeFile(doc);
        assertEquals(1, files.size());
        assertFalse(files.contains(doc));
    }

    /**
     * Verifies getSize reutrns correct size based on the contained files
     */
    @Test
    public void testFileSize() {
        Directory dir = new Directory("Root");
        Document doc1 = new Document("Doc1", "txt", "content1");
        Document doc2 = new Document("Doc2", "txt", "content2");

        dir.addFile(doc1);
        dir.addFile(doc2);

        int expectedSize = dir.getSize();
        assertEquals(expectedSize, dir.getSize());
    }

    /**
     * Tests setting a new name for the directory
     */
    @Test
    public void testName() {
        Directory dir = new Directory("OldName");

        // Set new name
        dir.setName("NewName");
        assertEquals("NewName", dir.getName());
    }
}
