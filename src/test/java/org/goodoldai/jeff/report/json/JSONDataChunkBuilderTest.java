/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.goodoldai.jeff.report.json;

import java.util.ArrayList;
import java.util.Iterator;
import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.OneDimData;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.explanation.data.ThreeDimData;
import org.goodoldai.jeff.explanation.data.Triple;
import org.goodoldai.jeff.explanation.data.Tuple;
import org.goodoldai.jeff.explanation.data.TwoDimData;
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
public class JSONDataChunkBuilderTest extends AbstractJeffTest {
    
    
    DataExplanationChunk singleDataChunk1;
    DataExplanationChunk singleDataChunk2;
    DataExplanationChunk oneDimDataChunk;
    DataExplanationChunk twoDimDataChunk;
    DataExplanationChunk threeDimDataChunk;
    JSONDataChunkBuilder jsonDataChunkBuilder;
    JSONObject jsonObject;

    /**
     * Creates a explanation.DataExplanationChunk, and org.json.simple.JSONObject instances
     * that are used for testing
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();

        jsonDataChunkBuilder = new JSONDataChunkBuilder();

        String[] tags = {"tag1", "tag2"};
        singleDataChunk1 = new DataExplanationChunk(new SingleData(new Dimension("testName"), "value"));
        singleDataChunk2 = new DataExplanationChunk(-10, "testGroup", "testRule", tags,
                new SingleData(new Dimension("testName", "testUnit"), "value"));

        ArrayList<Object> values = new ArrayList<Object>();
        values.add("value1");
        values.add("value2");
        oneDimDataChunk = new DataExplanationChunk(-10, "testGroup", "testRule", tags,
                new OneDimData(new Dimension("testName", "testUnit"), values));


        ArrayList<Tuple> tupleValues = new ArrayList<Tuple>();
        tupleValues.add(new Tuple("value1", "value2"));
        twoDimDataChunk = new DataExplanationChunk(-10, "testGroup", "testRule", tags,
                new TwoDimData(new Dimension("testName1", "testUnit1"), new Dimension("testName2", "testUnit2"), tupleValues));

        ArrayList<Triple> tripleValues = new ArrayList<Triple>();
        tripleValues.add(new Triple("value1", "value2", "value3"));
        threeDimDataChunk = new DataExplanationChunk(-10, "testGroup", "testRule", tags, new ThreeDimData(
                new Dimension("testName1", "testUnit1"),
                new Dimension("testName2", "testUnit2"),
                new Dimension("testName3", "testUnit3"),
                tripleValues));

        jsonObject = new JSONObject();
    }

    @After
    public void tearDown() {

        jsonDataChunkBuilder = null;

        singleDataChunk1 = null;
        singleDataChunk2 = null;
        oneDimDataChunk = null;
        twoDimDataChunk = null;
        threeDimDataChunk = null;

        jsonObject = null;

    }
    
    /**
     * Test of buildReportChunk method, of class JSONDataChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that only has content.
     */
    @Test
    public void testBuildReportChunkFirstConstructorSingleData() {
        jsonDataChunkBuilder.buildReportChunk(singleDataChunk1, jsonObject, true);
        
        //checks if there is an "dataExplanation" array
        assertTrue(jsonObject.get("dataExplanation") != null);
        
        JSONArray data = (JSONArray) jsonObject.get("dataExplanation");
        JSONObject jObj = (JSONObject) data.get(0);

        //checks the value of attribute of the element "dataExplanation"
        for (Iterator it = jObj.values().iterator(); it.hasNext();) {
            Object next = it.next();
            if(next instanceof String)
            {
                assertEquals("INFORMATIONAL".toLowerCase(), (String)next);
            }
        }

        
        //checks the values and names of elements of the element "value" (the content)
        JSONObject contentObj = (JSONObject) jObj.get("content");
        assertEquals("testName", contentObj.get("dimensionName"));
        assertEquals("value", contentObj.get("value"));
        
    }

    /**
     * Test of buildReportChunk method, of class JSONDataChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements and the type is SingleDimDataChunk.
     */
    @Test
    public void testBuildReportChunkSecondConstructorSingleData() {
        jsonDataChunkBuilder.buildReportChunk(singleDataChunk2, jsonObject, true);

        //the expected values
        String[] names = {"rule", "group", "context"};
        String[] values = {"testRule", "testGroup", "error"};
        String[] tags = {"tag1", "tag2"};
        String[] elements = {"tags", "content"};
        
         //checks if there is an "dataExplanation" array
        assertTrue(jsonObject.get("dataExplanation") != null);
        
        JSONArray data = (JSONArray) jsonObject.get("dataExplanation");
        JSONObject jObj = (JSONObject) data.get(0);
        //checks the values of properties of object "dataExplanation"
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

        JSONObject firstObj = (JSONObject) data.get(0);
      
        //checks the values and names of elements of the element "value" (the content)
        JSONObject contentObj = (JSONObject) firstObj.get("content");
        assertEquals("testName", contentObj.get("dimensionName"));
        assertEquals("testUnit", contentObj.get("dimensionUnit"));
        assertEquals("value", contentObj.get("value"));


    }

    /**
     * Test of buildReportChunk method, of class JSONDataChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements and the type is SingleDimDataChunk - but no chunk
     * headers are inserted.
     */
    @Test
    public void testBuildReportChunkSecondConstructorSingleDataNoChunkHeaders() {
        jsonDataChunkBuilder.buildReportChunk(singleDataChunk2, jsonObject, false);

        //the expected values
        String[] elements = {"content"};

        JSONArray data = (JSONArray) jsonObject.get("dataExplanation");
        //checks if "dataExplanation" array exists
        assertTrue(data != null);
        
         JSONObject jObj = (JSONObject) data.get(0);
        
        //checks the number of elements of the element "dataExplanation"
        assertEquals(1, jObj.keySet().size());
      
        //checks if there is "content" element inside "dataExplanation"
        assertTrue(jObj.get(elements[0]) != null);

        //checks the values and names of elements of the element "dataExplanation" (the content)
        JSONObject contentObj = (JSONObject) jObj.get("content");
        assertTrue(contentObj.get("dimensionName") != null);
        assertTrue(contentObj.get("dimensionUnit") != null);
        assertTrue(contentObj.get("value") != null);
        
        
        assertEquals("testName", contentObj.get("dimensionName"));
        assertEquals("testUnit", contentObj.get("dimensionUnit"));
        assertEquals("value", contentObj.get("value"));

    }

    /**
     * Test of buildReportChunk method, of class JSONDataChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements and the type is OneDimDataChunk.
     */
    @Test
    public void testBuildReportChunkSecondConstructorOneDimData() {
        jsonDataChunkBuilder.buildReportChunk(oneDimDataChunk, jsonObject, true);

        //the expected values
        String[] names = {"rule", "group", "context"};
        String[] values = {"testRule", "testGroup", "error"};
        String[] tags = {"tag1", "tag2"};
        String[] elements = {"tags", "content"};

        //checks if there is an "dataExplanation" array
        assertTrue(jsonObject.get("dataExplanation") != null);
        
        JSONArray data = (JSONArray) jsonObject.get("dataExplanation");
        JSONObject jObj = (JSONObject) data.get(0);
        //checks the values of properties of object "dataExplanation"
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

        JSONObject firstObj = (JSONObject) data.get(0);
        JSONObject contentObj = (JSONObject) firstObj.get("content");

    
        assertEquals("testName", contentObj.get("dimensionName"));
        assertEquals("testUnit", contentObj.get("dimensionUnit"));

        //the expected values of the content
        String[] oneDimValues = {"value1", "value2"};
        
        JSONArray valuesArray = (JSONArray) contentObj.get("values");
        for(int i=0; i<2; i++)
        {
            assertEquals(oneDimValues[i], ((JSONObject)valuesArray.get(i)).get("value"));
        }

    }

    /**
     * Test of buildReportChunk method, of class JSONDataChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements and the type is TwoDimDataChunk.
     */
    @Test
    public void testBuildReportChunkSecondConstructorTwoDimData() {
        jsonDataChunkBuilder.buildReportChunk(twoDimDataChunk, jsonObject, true);

        //the expected values
        String[] names = {"rule", "group", "context"};
        String[] values = {"testRule", "testGroup", "error"};
        String[] tags = {"tag1", "tag2"};
        String[] elements = {"tags", "content"};

        //checks if there is an "dataExplanation" array
        assertTrue(jsonObject.get("dataExplanation") != null);
        
        JSONArray data = (JSONArray) jsonObject.get("dataExplanation");
        JSONObject jObj = (JSONObject) data.get(0);
        //checks the values of properties of object "dataExplanation"
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

        JSONObject firstObj = (JSONObject) data.get(0);
        JSONObject contentObj = (JSONObject) firstObj.get("content");
        JSONObject tupleValue = (JSONObject) contentObj.get("tupleValue");

        //the expected values of the content
        String[] oneDimValues = {"value1", "value2"};
        String[] oneDimEllName = {"value1", "value2"};
        String[] oneDimAttName = {"testName1", "testName2"};
        String[] oneDimAttUnit = {"testUnit1", "testUnit2"};
        
         //checks the number of elements and attributes of the element "values" (the content)
        assertEquals(1, contentObj.keySet().size());
        assertEquals(2, tupleValue.keySet().size());

        //checks the values and names of elements of the element "tupleValue" (the content)
        for (int i = 0; i<2; i++)
        {
            JSONObject valueObject = (JSONObject) tupleValue.get(oneDimEllName[i]);
            assertEquals(oneDimValues[i], valueObject.get("value"));
            assertEquals(oneDimAttName[i], valueObject.get("dimensionName"));
            assertEquals(oneDimAttUnit[i], valueObject.get("dimensionUnit"));
        }
        
    }

    /**
     * Test of buildReportChunk method, of class JSONDataChunkBuilder.
     * Test case: successfull insertion of data using the ExplanationChunk constructor
     * that has all elements and the type is ThreeDimDataChunk.
     */
    @Test
    public void testBuildReportChunkSecondConstructorThreeDimData() {
        jsonDataChunkBuilder.buildReportChunk(threeDimDataChunk, jsonObject, true);

        //the expected values
        String[] names = {"rule", "group", "context"};
        String[] values = {"testRule", "testGroup", "error"};
        String[] tags = {"tag1", "tag2"};
        String[] elements = {"tags", "content"};
        
        //checks if there is an "dataExplanation" array
        assertTrue(jsonObject.get("dataExplanation") != null);
        
        JSONArray data = (JSONArray) jsonObject.get("dataExplanation");
        JSONObject jObj = (JSONObject) data.get(0);
        //checks the values of properties of object "dataExplanation"
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

        JSONObject firstObj = (JSONObject) data.get(0);
        JSONObject contentObj = (JSONObject) firstObj.get("content");
        JSONObject tripleValue = (JSONObject) contentObj.get("tripleValue");
        
        //checks the number of elements and attributes of the element "values" (the content)
        assertEquals(1, contentObj.keySet().size());
        assertEquals(3, tripleValue.keySet().size());

        //the expected values of the content
        String[] oneDimValues = {"value1", "value2", "value3"};
        String[] oneDimEllName = {"value1", "value2", "value3"};
        String[] oneDimAttName = {"testName1", "testName2", "testName3"};
        String[] oneDimAttUnit = {"testUnit1", "testUnit2", "testUnit3"};

        //checks the values and names of elements of the element "tripleValue" (the content)
        for (int i = 0; i<3; i++)
        {
            JSONObject valueObject = (JSONObject) tripleValue.get(oneDimEllName[i]);
            assertEquals(oneDimValues[i], valueObject.get("value"));
            assertEquals(oneDimAttName[i], valueObject.get("dimensionName"));
            assertEquals(oneDimAttUnit[i], valueObject.get("dimensionUnit"));
        }
        
    }

    /**
     * Test of buildReportChunk method, of class JSONDataChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    @Test
    public void testBuildReportChunkMissingAllArguments() {
        try {
            jsonDataChunkBuilder.buildReportChunk(null, null, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "All of the arguments are mandatory, so they can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class JSONDataChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the First null argument
     */
    @Test
    public void testBuildReportChunkMissingFirsttArgument() {
        try {
            jsonDataChunkBuilder.buildReportChunk(null, jsonObject, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'echunk' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class JSONDataChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument
     */
    @Test
    public void testBuildReportChunkMissingSecondArgument() {
        try {
            jsonDataChunkBuilder.buildReportChunk(singleDataChunk1, null, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class JSONDataChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the Firstt argument
     */
    @Test
    public void testBuildReportChunkWrongTypeFirstArgument() {
        try {
            jsonDataChunkBuilder.buildReportChunk(new TextExplanationChunk("testing.jpg"), jsonObject, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The ExplanationChunk must be type of DataExplanationChunk";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class JSONDataChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    @Test
    public void testBuildReportChunkWrongTypeSecondArgument() {
        try {
            jsonDataChunkBuilder.buildReportChunk( singleDataChunk2, "test", false );
            fail( "Exception should have been thrown, but it wasn't" );
        } catch ( Exception e ) {
            String result = e.getMessage();
            String expResult = "The stream must be the type of org.json.simple.JSONObject";
            assertTrue( e instanceof org.goodoldai.jeff.explanation.ExplanationException );
            assertEquals( expResult, result );
        }
    }
}
