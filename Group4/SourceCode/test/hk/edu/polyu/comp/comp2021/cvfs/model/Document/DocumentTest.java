package hk.edu.polyu.comp.comp2021.cvfs.model;

import org.junit.Test;

public class DocumentTest {

    /**
     * Create a new document with valid and invalid values and do testings
     */
    @Test
    public void testInitialization() {
        // Valid inputs
        Document doc1 = new Document("Doc1", "txt", "Sample content");
        assert doc1.getName().equals("Doc1");
        assert doc1.getType().equals("txt");
        assert doc1.getContent().equals("Sample content");

        // Invalid inputs
        Document doc2 = new Document("", "", "");
        assert doc2.getName().equals("");
        assert doc2.getType().equals("");
        assert doc2.getContent().equals("");
    }

    /**
     * Checks whether getsize returns correct size
     */
    @Test
    public void testgetSize() {
        Document doc = new Document("Doc", "txt", "content");
        int expectedSize = Document.size + "content".length() * 2;
        assert doc.getSize() == expectedSize : "Expected: " + expectedSize + " but got: " + doc.getSize();
    }

    /**
     * Test to set a new name for the document
     */
    @Test
    public void testNameManagement() {
        Document doc = new Document("Doc", "txt", "Sample content");

        // Set new name
        doc.setName("NewDoc");
        assert doc.getName().equals("NewDoc");
    }
}
