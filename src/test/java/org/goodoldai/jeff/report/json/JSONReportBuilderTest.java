/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.goodoldai.jeff.report.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.report.ReportBuilder;
import org.goodoldai.jeff.report.ReportBuilderTest;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author darkostojkovic
 */
public class JSONReportBuilderTest extends ReportBuilderTest{
    
    Explanation explanation1;
    Explanation explanation2;
    JSONObject jsonObject1;
    JSONObject jsonObject2;

    @Override
    public ReportBuilder getInstance(ReportChunkBuilderFactory factory) {
        return new JSONReportBuilder(factory);
    }

    @Override
    public ReportChunkBuilderFactory getFactory() {
        return new JSONChunkBuilderFactory();
    }

    /**
     * Creates a explanation.TextExplanationChunk, explanation.ImageExplanationChunk,
     * explanation.DataExplanationChunk, and org.json.simple.JSONObject instances that are
     * used for testing
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();

        instance = new JSONReportBuilder(new JSONChunkBuilderFactory());

        explanation1 = new Explanation("tester");

        explanation2 = new Explanation("tester", "EN", "USA", "explanation title");
        explanation2.addChunk(new TextExplanationChunk("textTest"));
        explanation2.addChunk(new ImageExplanationChunk(new ImageData("imgTest.jpg")));

        String[] tags = {"tag1", "tag2"};
        explanation2.addChunk(new DataExplanationChunk(-10, "testGroup", "testRule", tags,
                new SingleData(new Dimension("testName", "testUnit"), "value")));

        jsonObject1 = new JSONObject();

        jsonObject2 = new JSONObject();
    }

    @After
    public void tearDown() {
        new File("test.json").delete();
    }
    
    
    /**
     * Test of buildReport method (this method takes the name of the file and
     * calls the other buildReport method of the same class), of class JSONReportBuilder.
     * Test case: successful building of explanation
     */
    @Test
    public void testBuildReportMainMethod() throws IOException, ParseException {
        instance.buildReport(explanation2, "test.json");

        //checks if the file is created
        assertTrue(new File("test.json").exists());

        JSONParser parser = new JSONParser();
        
        JSONObject parsedObject = (JSONObject) parser.parse(new FileReader("test.json"));

        //checks the values of top-level properties
        assertEquals("tester", parsedObject.get("owner"));
        assertEquals("EN", parsedObject.get("language"));
        assertEquals("USA", parsedObject.get("country"));
        assertEquals(DateFormat.getInstance().format(explanation2.getCreated().getTime()), parsedObject.get("date"));
        assertEquals("explanation title", parsedObject.get("title"));

        //checks the names of elements which hold the explanation chunks which were tested in other mehtods and classes
        assertTrue(parsedObject.get("textualExplanation") != null);
        assertTrue(parsedObject.get("imageExplanation") != null);
        assertTrue(parsedObject.get("dataExplanation") != null);

    }

    /**
     * Test of buildReport method, of class JSONReportBuilder.
     * Test case: successful building of explanation
     */
    @Test
    public void testBuildReport() throws FileNotFoundException, IOException, ParseException {
        instance.buildReport(explanation2, new PrintWriter(new File("test.json")));

        //checks if the file is created
        assertTrue(new File("test.json").exists());

        JSONParser parser = new JSONParser();
        
        JSONObject parsedObject = (JSONObject) parser.parse(new FileReader("test.json"));

        //checks the values of top-level properties
        assertEquals("tester", parsedObject.get("owner"));
        assertEquals("EN", parsedObject.get("language"));
        assertEquals("USA", parsedObject.get("country"));
        assertEquals(DateFormat.getInstance().format(explanation2.getCreated().getTime()), parsedObject.get("date"));
        assertEquals("explanation title", parsedObject.get("title"));

        //checks the names of elements which hold the explanation chunks which were tested in other mehtods and classes
        assertTrue(parsedObject.get("textualExplanation") != null);
        assertTrue(parsedObject.get("imageExplanation") != null);
        assertTrue(parsedObject.get("dataExplanation") != null);

    }

    /**
     * Test of buildReport method, of class JSONReportBuilder.
     * Test case: successful building of explanation - but with no chunk headers
     */
    @Test
    public void testBuildReportNoChunkHeaders() throws FileNotFoundException, IOException, ParseException {
        instance.setInsertChunkHeaders(false);
        instance.buildReport(explanation2, new PrintWriter(new File("test.json")));

        //checks if the file is created
        assertTrue(new File("test.json").exists());

        JSONParser parser = new JSONParser();
        
        JSONObject parsedObject = (JSONObject) parser.parse(new FileReader("test.json"));

        //checks the values of top-level properties
        assertEquals("tester", parsedObject.get("owner"));
        assertEquals("EN", parsedObject.get("language"));
        assertEquals("USA", parsedObject.get("country"));
        assertEquals(DateFormat.getInstance().format(explanation2.getCreated().getTime()), parsedObject.get("date"));
        assertEquals("explanation title", parsedObject.get("title"));

        //checks the names of elements which hold the explanation chunks which were tested in other mehtods and classes
        assertTrue(parsedObject.get("textualExplanation") != null);
        assertTrue(parsedObject.get("imageExplanation") != null);
        assertTrue(parsedObject.get("dataExplanation") != null);
        
        
        JSONArray txts = (JSONArray) parsedObject.get("textualExplanation");
        JSONArray data = (JSONArray) parsedObject.get("dataExplanation");
        JSONArray imgs = (JSONArray) parsedObject.get("imageExplanation");
        //checks the number of attributes and elements in the chunks in order to
        //ensure that the chunk headers were not inserted
        assertEquals(0, ((JSONObject) txts.get(0)).keySet().size() - 1);
        assertEquals(0, ((JSONObject) imgs.get(0)).keySet().size() - 1);
        assertEquals(0, ((JSONObject) data.get(0)).keySet().size() - 1);
        assertEquals(1, txts.size());
        assertEquals(1, imgs.size());
        assertEquals(1, data.size());


    }

    /**
     * Test of buildReport method, of class JSONReportBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    @Test
    public void testBuildReportWrongTypeSecondArgument() {
        try {
            instance.buildReport(explanation1, new Object());
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' must be the type of java.io.PrintWriter";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }    

    /**
     * Test of insertHeader method, of class JSONReportBuilder.
     * Test case: successful building of a chunk, inserting header information,
     * using Explanation constructor that only has "owner" attribute.
     */
    @Test
    public void testInsertHeader() {
        ((JSONReportBuilder)instance).insertHeader(explanation1, jsonObject1);

        //checks the values of attributes
        assertEquals("tester", jsonObject1.get("owner"));
        assertEquals(DateFormat.getInstance().format(explanation2.getCreated().getTime()), jsonObject1.get("date"));
    }

    /**
     * Test of insertHeader method, of class JSONReportBuilder.
     * Test case: successful building of a chunk, inserting header information
     * using Explanation constructor that only has all the attributes.
     */
    @Test
    public void testInsertHeaderAllInfo() {
        ((JSONReportBuilder)instance).insertHeader(explanation2, jsonObject1);

        //checks the values of attributes
        assertEquals("tester", jsonObject1.get("owner"));
        assertEquals("EN", jsonObject1.get("language"));
        assertEquals("USA", jsonObject1.get("country"));
        assertEquals(DateFormat.getInstance().format(explanation2.getCreated().getTime()), jsonObject1.get("date"));
        assertEquals("explanation title", jsonObject1.get("title"));


    }

    /**
     * Test of insertHeader method, of class JSONReportBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    @Test
    public void testInsertHeaderMissingAllArguments() {
        try {
            ((JSONReportBuilder)instance).insertHeader(null, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "All of the arguments are mandatory, so they can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of insertHeader method, of class JSONReportBuilder.
     * Test case: unsuccessful building of a chunk because of the First null argument
     */
    @Test
    public void testInsertHeaderMissingFirstArgument() {
        try {
            ((JSONReportBuilder)instance).insertHeader(null, jsonObject1);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'explanation' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of insertHeader method, of class JSONReportBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument
     */
    @Test
    public void testInsertHeaderMissingSecondArgument() {
        try {
            ((JSONReportBuilder)instance).insertHeader(explanation1, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }


    /**
     * Test of insertHeader method, of class JSONReportBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    @Test
    public void testInsertHeaderWrongTypeSecondArgument() {
        try {
            ((JSONReportBuilder)instance).insertHeader(explanation1, "test");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' must be the type of org.json.simple.JSONObject";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }
}
