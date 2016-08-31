/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.goodoldai.jeff.report.json;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
public class JSONTextChunkBuilderTest extends AbstractJeffTest {
    JSONObject jsonObject;
    TextExplanationChunk textEchunk1;
    TextExplanationChunk textEchunk2;
    JSONTextChunkBuilder instance;
    
    /**
     * Creates a explanation.TextExplanationChunk, and org.json.simple.JSONObject instances
     * that are used for testing
     */
    @Before
    public void setUp() throws Exception {
        super.setUp(); 
        String[] tags = {"tag1", "tag2"};
        
        instance = new JSONTextChunkBuilder();
        
        textEchunk1 = new TextExplanationChunk("testing");
        textEchunk2 = new TextExplanationChunk(-10, "testGroup", "testRule", tags, "test text");
        
        jsonObject = new JSONObject();
    }
    
      @After
    public void tearDown() {

        instance = null;

        textEchunk1 = null;
        textEchunk2 = null;

        jsonObject = null;
    }
    

    /**
     * Test of buildReportChunk method, of class JSONTextChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    @Test
    public void testBuildReportChunkMissingAllArguments() {
        try {
            instance.buildReportChunk(null, null, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "All of the arguments are mandatory, so they can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class JSONTextChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the first null argument
     */
    @Test
    public void testBuildReportChunkMissingFirstArgument() {
        try {
            instance.buildReportChunk(null, jsonObject, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'echunk' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class JSONTextChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument
     */
    @Test
    public void testBuildReportChunkMissingSecondArgument() {
        try {
            instance.buildReportChunk(textEchunk1, null, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLTextChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the first argument
     */
    @Test
    public void testBuildReportChunkWrongTypeFirsArgument() {
        try {
            instance.buildReportChunk(new ImageExplanationChunk(new ImageData("testing.jpg")), jsonObject, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The ExplanationChunk must be the type of TextExplanationChunk";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class JSONTextChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    @Test
    public void testBuildReportChunkWrongTypeSecondArgument() {
        try {
            instance.buildReportChunk(textEchunk1, "test", false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The stream must be the type of org.json.simple.JSONObject";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }
    
        /**
     * Test of buildReportChunk method, of class JSONTextChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that only has content.
     */
    @Test
    public void testBuildReportChunkFirstConstructor() {
        instance.buildReportChunk(textEchunk1, jsonObject, true);

        //checks if there is an element named "textualExplanation"
        assertTrue(jsonObject.get("textualExplanation") != null);

        //checks if there is at least one element inside "textualExplanation" array
        assertTrue(((JSONArray)jsonObject.get("textualExplanation")).size() > 0);
        
        //checks if "content" attribute of textualExplanation has the proper value
        for(Object o :(JSONArray) jsonObject.get("textualExplanation"))
        {
            JSONObject jObj = (JSONObject) o;
            assertTrue(jObj.containsKey("content"));
             assertEquals(textEchunk1.getContent(), jObj.get("content"));
        }

    }
    
    /**
     * Test of buildReportChunk method, of class XMLTextChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements.
     */
    @Test
    public void testBuildReportChunkSecondConstructor() {
        instance.buildReportChunk(textEchunk2, jsonObject, true);

        //the expected values
        String[] names = {"rule", "group", "context"};
        String[] values = {"testRule", "testGroup", "error"};
        String[] tags = {"tag1", "tag2"};
        String[] elements = {"tags", "content"};

        //checks if there is an element named "textualExplanation"
        assertTrue(jsonObject.get("textualExplanation") != null);

        //checks the number of elements of the element "textualExplanation"
        JSONArray textExplanation = (JSONArray)jsonObject.get("textualExplanation");
        assertEquals(1, textExplanation.size());

        //checks the values of attributes of the element "textualExplanation"
        int i = 0;
        for (Object o :(JSONArray) jsonObject.get("textualExplanation"))
        {
            JSONObject jsonIter = (JSONObject) o;
            assertTrue(jsonIter.get(names[i])!= null);
            assertEquals(jsonIter.get(names[i]),values[i]);
            i++;
        }

        //checks if the "content" attribute of "textualExplanation" exists
        for (Object o :(JSONArray) jsonObject.get("textualExplanation"))
        {
            JSONObject jsonIter = (JSONObject) o;
            assertTrue(jsonIter.get("content")!= null);
        }

        //checks the values and names of elements of the element "tags"
        int k = 0;
        for (Object o : textExplanation)
        {
            JSONObject jsonIter = (JSONObject) o;
            assertTrue(jsonIter.get("tags")!= null);
            for (Object obj : (JSONArray) jsonIter.get("tags"))
            {
                JSONObject jTag = (JSONObject) obj;
                assertTrue(jTag.get("tag")!=null);
                assertEquals(jTag.get("tag"),tags[k]);
                k++;
            }
        }

         //checks the values and names of elements of the element "content" (the content)
        assertEquals("test text", ((JSONObject)textExplanation.get(0)).get("content"));
    }

    
}
