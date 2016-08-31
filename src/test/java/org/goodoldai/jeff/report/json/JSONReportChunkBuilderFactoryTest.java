/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.goodoldai.jeff.report.json;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.report.ReportChunkBuilder;
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
public class JSONReportChunkBuilderFactoryTest extends AbstractJeffTest {
    
    JSONChunkBuilderFactory instance;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        instance = new JSONChunkBuilderFactory();
    }

    @After
    public void tearDown(){
        instance = null;
    }
    
    /**
     * Test of getReportChunkBuilder method, of class JSONChunkBuilderFactory.
     * Test case: unsuccessful execution - chunk type not recognized
     */
    @Test
    public void testGetXMLReportChunkBuilderTypeNotRecognized() {
        try {
            instance.getReportChunkBuilder(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The provided ExplanationChunk does not match any of the required types";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getReportChunkBuilder method, of class JSONChunkBuilderFactory.
     * Test case: successful execution, passing of TEXT Explanation Chunk type
     */
    @Test
    public void testGetXMLReportChunkBuilderText1() {

        ReportChunkBuilder result = instance.getReportChunkBuilder(new TextExplanationChunk("testText"));

        assertTrue(result instanceof JSONTextChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class JSONChunkBuilderFactory.
     * Test case: successful execution, passing of TEXT Explanation chunk type, assert returning
     * of same instance every time
     */
    @Test
    public void testGetXMLReportChunkBuilderText2() {

        ReportChunkBuilder result1 = instance.getReportChunkBuilder(new TextExplanationChunk("testText1"));
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(new TextExplanationChunk("testText2"));

        assertTrue(result1 instanceof JSONTextChunkBuilder);
        assertTrue(result2 instanceof JSONTextChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getReportChunkBuilder method, of class JSONChunkBuilderFactory.
     * Test case: successful execution, passing of IMAGE Explanation chunk type
     */
    @Test
    public void testGetXMLReportChunkBuilderImage1() {

        ReportChunkBuilder result = instance.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("test.jpg")));

        assertTrue(result instanceof JSONImageChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class JSONChunkBuilderFactory.
     * Test case: successful execution, passing of  IMAGE Explanation chunk type, assert returning
     * of same instance every time
     */
    @Test
    public void testGetXMLReportChunkBuilderImage2() {

        ReportChunkBuilder result1 = instance.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("test1.jpg")));
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("test2.jpg")));

        assertTrue(result1 instanceof JSONImageChunkBuilder);
        assertTrue(result2 instanceof JSONImageChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getReportChunkBuilder method, of class JSONReportChunkBuilderFactory.
     * Test case: successful execution, passing of DATA Explanation chunk type
     */
    @Test
    public void testGetXMLReportChunkBuilderData1() {

        ReportChunkBuilder result = instance.getReportChunkBuilder(
                new DataExplanationChunk(new SingleData(new Dimension("test"), "test")));

        assertTrue(result instanceof JSONDataChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class JSONChunkBuilderFactory.
     * Test case: successful execution, passing of  DATA Explanation chunk type, assert returning
     * of same instance every time
     */
    @Test
    public void testGetXMLReportChunkBuilderData2() {
        ReportChunkBuilder result1 = instance.getReportChunkBuilder(
                new DataExplanationChunk(new SingleData(new Dimension("test"), "test")));
        
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(
                new DataExplanationChunk(new SingleData(new Dimension("test"), "test")));

        assertTrue(result1 instanceof JSONDataChunkBuilder);
        assertTrue(result2 instanceof JSONDataChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }
    
}
