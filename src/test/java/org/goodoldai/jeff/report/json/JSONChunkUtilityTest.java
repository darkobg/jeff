/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.goodoldai.jeff.report.json;

import java.util.Iterator;
import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author darkostojkovic
 */
public class JSONChunkUtilityTest extends AbstractJeffTest {
    TextExplanationChunk textEchunk1;
    TextExplanationChunk textEchunk2;
    JSONObject jsonObject;
    
    /**
     * Creates a explanation.TextExplanationChunk, explanation.ImageExplanationChunk,
     * explanation.DataExplanationChunk, and org.json.simple.JSONObject instances that are
     * used for testing
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();

        String[] tags = {"tag1", "tag2"};

        textEchunk1 = new TextExplanationChunk("testing");
        textEchunk2 = new TextExplanationChunk(-10, "testGroup", "testRule", tags, "test text");

        jsonObject = new JSONObject();
    }
    
    @After
    public void tearDown() {

        textEchunk1 = null;
        textEchunk2 = null;
        
        jsonObject = null;
    }
    
    /**
     * Test of insertExplanationInfo method, of class JSONChunkUtility.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that only has content.
     */
    @Test
    public void testInsertExplenationInfoFirstConstructor() {

        JSONChunkUtility.insertExplanationInfo(textEchunk1, jsonObject);

        //checks the number of attributes and elements
        assertEquals(1, jsonObject.keySet().size());

        //checks the value of attribute
        for (Iterator it = jsonObject.keySet().iterator(); it.hasNext();) {
            String objectKey = (String) it.next();
            assertEquals("INFORMATIONAL".toLowerCase(), jsonObject.get(objectKey));
        }
    }
    
    /**
     * Test of insertExplanationInfo method, of class JSONChunkUtility.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements.
     */
    @Test
    public void testInsertExplanationInfoSecondConstructor() {


        String[] names = {"rule", "group", "context"};
        String[] values = {"testRule", "testGroup", "error"};
        String[] tags = {"tag1", "tag2"};

        JSONChunkUtility.insertExplanationInfo(textEchunk2, jsonObject);
        
        //checks the number of elements in the element "tags"
        assertEquals(2, ((JSONArray) jsonObject.get("tags")).size());

        //checks value , group and context attributes
        for(String attributeName : names)
        {
            assertTrue(jsonObject.get(attributeName)!= null);
        }

        //checks for the existance of tags
        assertTrue(jsonObject.get("tags")!= null);

        //checks the values and names of element "tags"
        int i = 0;
        for(Object o : ((JSONArray)jsonObject.get("tags")))
        {
            JSONObject jObj = (JSONObject) o;
            assertEquals(jObj.get("tag"), tags[i++]);
        }
    }
    
    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is not predefined
     */
    @Test
    public void testTranslateContextTypeNotRecognized() {
        int context = -555;
        String result = JSONChunkUtility.translateContext(context, textEchunk1);

        assertEquals(String.valueOf(context), result);
    }

    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "INFORMATIONAL"
     */
    @Test
    public void testTranslateContextKnownTypeInformational() {
        int context = 0;
        String result = JSONChunkUtility.translateContext(context, textEchunk1);
        String expResult = "INFORMATIONAL".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "WARNING"
     */
    @Test
    public void testTranslateContextKnownTypeInformationalWarning() {
        int context = -5;
        String result = JSONChunkUtility.translateContext(context, textEchunk1);
        String expResult = "WARNING".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "ERROR"
     */
    @Test
    public void testTranslateContextKnownTypeError() {
        int context = -10;
        String result = JSONChunkUtility.translateContext(context, textEchunk1);
        String expResult = "ERROR".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "POSITIVE"
     */
    @Test
    public void testTranslateContextKnownTypePositive() {
        int context = 1;
        String result = JSONChunkUtility.translateContext(context, textEchunk1);
        String expResult = "POSITIVE".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "VERY_POSITIVE"
     */
    @Test
    public void testTranslateContextKnownTypeVeryPositive() {
        int context = 2;
        String result = JSONChunkUtility.translateContext(context, textEchunk1);
        String expResult = "VERY_POSITIVE".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "NEGATIVE"
     */
    @Test
    public void testTranslateContextKnownTypeNegative() {
        int context = -1;
        String result = JSONChunkUtility.translateContext(context, textEchunk1);
        String expResult = "NEGATIVE".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "VERY_NEGATIVE"
     */
    @Test
    public void testTranslateContextKnownTypeVeryNegative() {
        int context = -2;
        String result = JSONChunkUtility.translateContext(context, textEchunk1);
        String expResult = "VERY_NEGATIVE".toLowerCase();
        assertEquals(expResult, result);
    }

}
