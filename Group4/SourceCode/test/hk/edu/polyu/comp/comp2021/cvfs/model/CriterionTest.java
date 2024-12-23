package hk.edu.polyu.comp.comp2021.cvfs.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test with different cases for two evaluations
 * (cover various attributes, operatos, values and logical operators)
 */
public class CriterionTest {

    /**
     * Test different attributes with various opetators
     */
    @Test
    public void testValidNameCriterion() {
        SimpleCriterion criterion = new SimpleCriterion("name", "\"contains\"", "\"test\"");
        Document doc = new Document("testDocument", "txt", "100");
        assertTrue(criterion.evaluate(doc));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNameCriterion() {
        new SimpleCriterion("name", "invalidOp", "\"test\"");
    }

    @Test
    public void testValidTypeCriterion() {
        SimpleCriterion criterion = new SimpleCriterion("type", "\"equals\"", "\"txt\"");
        Document doc = new Document("testDocument", "txt", "a");
        assertTrue(criterion.evaluate(doc));
    }

    @Test
    public void testValidSizeCriterion() {
        SimpleCriterion criterion = new SimpleCriterion("size", "\">\"", "50");
        Document doc = new Document("testDocument", "txt", "asssdasdasd");
        assertTrue(criterion.evaluate(doc));
    }

    @Test
    public void testInvalidSizeCriterion() {
        SimpleCriterion criterion = new SimpleCriterion("size", "\">\"", "150");
        Document doc = new Document("testDocument", "txt", "asssdasdasd");
        assertFalse(criterion.evaluate(doc));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParameters() {
        new SimpleCriterion(null, "\"contains\"", "\"test\"");
    }

    @Test
    public void testToString() {
        SimpleCriterion criterion = new SimpleCriterion("name", "\"contains\"", "\"test\"");
        assertEquals("SimpleCriterion(attrName: name, op: \"contains\", val: \"test\")", criterion.toString());
    }

    /**
     * evaluate CompositeCriterion with logical combinations amd test with various operations
     */
    @Test
    public void testAndOperation() {
        Criterion cr = new SimpleCriterion("name", "\"contains\"", "\"test\"");
        Criterion ca = new SimpleCriterion("type", "\"equals\"", "\"txt\"");
        CompositeCriterion compositeCriterion = new CompositeCriterion(cr, ca, "&&");

        Document doc = new Document("testDocument", "txt", "123");
        assertTrue(compositeCriterion.evaluate(doc));
    }

    @Test
    public void testOrOperation() {
        Criterion cr = new SimpleCriterion("name", "\"contains\"", "\"test\"");
        Criterion ca = new SimpleCriterion("type", "\"equals\"", "\"pdf\"");
        CompositeCriterion compositeCriterion = new CompositeCriterion(cr, ca, "||");

        Document doc = new Document("testDocument", "txt", "234234");
        assertTrue(compositeCriterion.evaluate(doc));
    }

    @Test
    public void testNotOperation() {
        Criterion cr = new SimpleCriterion("name", "\"contains\"", "\"test\"");
        CompositeCriterion compositeCriterion = new CompositeCriterion(cr, null, "!");

        Document doc = new Document("exampleDocument", "txt", "100");
        assertTrue(compositeCriterion.evaluate(doc));
    }

    @Test
    public void testToStringComp() {
        Criterion cr = new SimpleCriterion("name", "\"contains\"", "\"test\"");
        Criterion ci = new SimpleCriterion("type", "\"equals\"", "\"txt\"");
        CompositeCriterion compositeCriterion = new CompositeCriterion(cr, ci, "&&");

        assertEquals("(SimpleCriterion(attrName: name, op: \"contains\", val: \"test\") && SimpleCriterion(attrName: type, op: \"equals\", val: \"txt\"))", compositeCriterion.toString());
    }
}
