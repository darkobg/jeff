/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.goodoldai.jeff.report.json;

import java.util.Iterator;
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
public class JSONImageChunkBuilderTest extends AbstractJeffTest {
    
    ImageExplanationChunk imageEchunk1;
    ImageExplanationChunk imageEchunk2;
    JSONImageChunkBuilder jsonImageChunkBuilder;
    JSONObject jsonObject;
    
    /**
     * Creates a explanation.ImageExplanationChunk, and org.json.simple.JSONObject instances
     * that are used for testing
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();

        String[] tags = {"tag1", "tag2"};

        jsonImageChunkBuilder = new JSONImageChunkBuilder();

        imageEchunk1 = new ImageExplanationChunk(new ImageData("picture.jpg"));        
        imageEchunk2 = new ImageExplanationChunk(-10, "testGroup", "testRule", tags, new ImageData("picture.jpg", "picture"));

        jsonObject = new JSONObject();
    }

    @After
    public void tearDown() {

        jsonImageChunkBuilder = null;

        imageEchunk1 = null;
        imageEchunk2 = null;

        jsonObject = null;
       
    }

    /**
     * Test of buildReportChunk method, of class JSONImageChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that only has content.
     */
    @Test
    public void testBuildReportChunkFirstConstructor() {
        jsonImageChunkBuilder.buildReportChunk(imageEchunk1, jsonObject, true);

       //checks if there is an "imageExplanation" array
        assertTrue(jsonObject.get("imageExplanation") != null);
        
        JSONArray data = (JSONArray) jsonObject.get("imageExplanation");
        JSONObject jObj = (JSONObject) data.get(0);

        //checks the values and names of elements of the element "imageExplanation"
        assertEquals("picture.jpg", ((JSONObject)jObj.get("content")).get("imageUrl"));
    }

    /**
     * Test of buildReportChunk method, of class JSONImageChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements.
     */
    @Test
    public void testBuildReportChunkSecondConstructor() {
        jsonImageChunkBuilder.buildReportChunk(imageEchunk2, jsonObject, true);

        //the expected values
        String[] names = {"rule", "group", "context"};
        String[] values = {"testRule", "testGroup", "error"};
        String[] tags = {"tag1", "tag2"};
        String[] elements = {"tags", "content"};
        
         //checks if there is an "dataExplanation" array
        assertTrue(jsonObject.get("imageExplanation") != null);
        
        JSONArray data = (JSONArray) jsonObject.get("imageExplanation");
        JSONObject jObj = (JSONObject) data.get(0);
        //checks the values of properties of object "imageExplanation"
        for(int i=0;i<3;i++)
        {
            assertEquals(values[i],jObj.get(names[i]));
        }
        
        //check number of tags
        JSONArray tagsArray = (JSONArray) jObj.get("tags");
        assertEquals(2, tagsArray.size());
        
        //check tag values
        int k = 0;
        for(Object o : tagsArray)
        {
            JSONObject tag = (JSONObject) o;
            assertEquals(tag.get("tag"), tags[k++]);
        }
        

        //checks the values and names of elements of the element "imageExplanation" (the content)
        assertEquals("picture", ((JSONObject)jObj.get("content")).get("caption"));
        assertEquals("picture.jpg", ((JSONObject)jObj.get("content")).get("imageUrl"));

    }

    /**
     * Test of buildReportChunk method, of class JSONImageChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements - but with no chunk headers inserted.
     */
    @Test
    public void testBuildReportChunkSecondConstructorWithNoChunkHeaders() {
        jsonImageChunkBuilder.buildReportChunk(imageEchunk2, jsonObject, false);

        //the expected values
        String[] elements = {"content"};

        JSONArray imgs = (JSONArray) jsonObject.get("imageExplanation");
        //checks if "dataExplanation" array exists
        assertTrue(imgs != null);
        
         JSONObject jObj = (JSONObject) imgs.get(0);
        
        //checks the number of elements of the element "dataExplanation"
        assertEquals(1, jObj.keySet().size());

        //checks the values and names of elements of the element "imageExplanation"
        int j = 0;
        for (Iterator it = jObj.keySet().iterator(); it.hasNext();) {
            String element = (String) it.next();
            assertEquals(elements[j++], element);
        }
        assertTrue(jObj.get("content") != null);

        //checks the values and names of elements of the element "imageExplanation" (the content)
        assertEquals("picture", ((JSONObject)jObj.get("content")).get("caption"));
        assertEquals("picture.jpg", ((JSONObject)jObj.get("content")).get("imageUrl"));

    }

    /**
     * Test of buildReportChunk method, of class JSONImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    @Test
    public void testBuildReportChunkMissingAllArguments() {
        try {
            jsonImageChunkBuilder.buildReportChunk(null, null, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "All of the arguments are mandatory, so they can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class JSONImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the first null argument
     */
    @Test
    public void testBuildReportChunkMissingFirstArgumant() {
        try {
            jsonImageChunkBuilder.buildReportChunk(null, jsonObject, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'echunk' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class JSONImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument
     */
    @Test
    public void testBuildReportChunkMissingSecondArgumant() {
        try {
            jsonImageChunkBuilder.buildReportChunk(imageEchunk1, null, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class JSONImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the first argument
     */
    @Test
    public void testBuildReportChunkWrongTypeFirsArgumant() {
        try {
            jsonImageChunkBuilder.buildReportChunk(new TextExplanationChunk("testing.jpg"), jsonObject, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The ExplanationChunk must be type of ImageExplanationChunk";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class JSONImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    @Test
    public void testBuildReportChunkWrongTypeSecondArgumant() {
        try {
            jsonImageChunkBuilder.buildReportChunk(imageEchunk1, "test", false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The stream must be the type of org.json.simple.JSONObject";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }
    
}
