/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.goodoldai.jeff.report.json;

import java.util.ArrayList;
import java.util.Iterator;
import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.OneDimData;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.explanation.data.ThreeDimData;
import org.goodoldai.jeff.explanation.data.Triple;
import org.goodoldai.jeff.explanation.data.Tuple;
import org.goodoldai.jeff.explanation.data.TwoDimData;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author darkostojkovic
 */
public class JSONDataChunkBuilder implements ReportChunkBuilder{
    
    
    public JSONDataChunkBuilder(){
        
    }

    public void buildReportChunk(ExplanationChunk echunk, Object stream, boolean insertHeaders) {
        if (echunk == null && stream == null) {
            throw new ExplanationException("All of the arguments are mandatory, so they can not be null");
        }

        if (echunk == null) {
            throw new ExplanationException("The argument 'echunk' is mandatory, so it can not be null");
        }

        if (stream == null) {
            throw new ExplanationException("The argument 'stream' is mandatory, so it can not be null");
        }

        if (!(echunk instanceof DataExplanationChunk)) {
            throw new ExplanationException("The ExplanationChunk must be type of DataExplanationChunk");
        }

        if (!(stream instanceof JSONObject)) {
            throw new ExplanationException("The stream must be the type of org.dom4j.Document");
        }
        
        JSONObject jObject = (JSONObject) stream;
        
        JSONObject jsonDataExp = new JSONObject();
        if(insertHeaders)
            JSONChunkUtility.insertExplanationInfo(echunk, jsonDataExp);
        
//        Document document = (Document) stream;
//        Element element = document.getRootElement().addElement("dataExplanation");
//
//        if (insertHeaders)
//            XMLChunkUtility.insertExplanationInfo(echunk, element);

        DataExplanationChunk dataExplenationChunk = (DataExplanationChunk) echunk;

        insertContent(dataExplenationChunk, jsonDataExp);
        
        jObject.put("dataExplanation", jsonDataExp);
    }
    
    
    private void insertContent(DataExplanationChunk dataExplenationChunk, JSONObject jObject) {

        if (dataExplenationChunk.getContent() instanceof SingleData) {
            inputSingleDataContent(dataExplenationChunk, jObject);

        } else if (dataExplenationChunk.getContent() instanceof OneDimData) {
            inputOneDimDataContent(dataExplenationChunk, jObject);

        } else if (dataExplenationChunk.getContent() instanceof TwoDimData) {
            inputTwoDimDataContent(dataExplenationChunk, jObject);

        } else if (dataExplenationChunk.getContent() instanceof ThreeDimData) {
            inputThreeDimDataContent(dataExplenationChunk, jObject);
            
        }
    }
    
    
    private void inputSingleDataContent(DataExplanationChunk dataExplenationChunk, JSONObject jObject) {
        SingleData singleData = (SingleData) dataExplenationChunk.getContent();

        String value = String.valueOf(singleData.getValue());

        Dimension dimension = singleData.getDimension();
        String dimensionName = dimension.getName();
        String dimensionUnit = dimension.getUnit();

        JSONObject contentObj = new JSONObject();
        contentObj.put("value", value);
        contentObj.put("dimensionName", dimensionName);
        if (dimensionUnit != null) {
            contentObj.put("dimensionUnit", dimensionUnit);
        }
        jObject.put("content", contentObj);
    }
    
    private void inputOneDimDataContent(DataExplanationChunk dataExplenationChunk, JSONObject jObject) {
        OneDimData oneDimData = (OneDimData) dataExplenationChunk.getContent();

        ArrayList<Object> objectValues = oneDimData.getValues();
        ArrayList<String> values = new ArrayList<String>();

        for (Iterator<Object> it = objectValues.iterator(); it.hasNext();) {
            Object object = it.next();
            values.add(String.valueOf(object));
        }

        Dimension dimension = oneDimData.getDimension();
        String dimensionName = dimension.getName();
        String dimensionUnit = dimension.getUnit();

        JSONObject contentObj = new JSONObject();
        
        JSONArray valuesArray = new JSONArray();
        for (Iterator<String> it = values.iterator(); it.hasNext();) {
            String value = it.next();
            valuesArray.add(new JSONObject().put("value", value));
        }
        contentObj.put("values", valuesArray);
        contentObj.put("dimensionName", dimensionName);
        if (dimensionUnit != null) {
            contentObj.put("dimensionUnit", dimensionUnit);
        }
        
        jObject.put("content", contentObj);
        
    }

    private void inputTwoDimDataContent(DataExplanationChunk dataExplenationChunk, JSONObject jObject) {
        TwoDimData twoDimData = (TwoDimData) dataExplenationChunk.getContent();

        ArrayList<Tuple> tupleValues = twoDimData.getValues();

        Dimension dimension1 = twoDimData.getDimension1();
        String dimensionName1 = dimension1.getName();
        String dimensionUnit1 = dimension1.getUnit();

        Dimension dimension2 = twoDimData.getDimension2();
        String dimensionName2 = dimension2.getName();
        String dimensionUnit2 = dimension2.getUnit();

        JSONObject contentObj = new JSONObject(); 
        JSONObject tupleObj = new JSONObject();

        for (Iterator<Tuple> it = tupleValues.iterator(); it.hasNext();) {
            Tuple tuple = it.next();

            String value1 = String.valueOf(tuple.getValue1());
            String value2 = String.valueOf(tuple.getValue2());

            JSONObject valueObj1 = new JSONObject();
            valueObj1.put("value", value1);
            valueObj1.put("dimensionName", dimensionName1);
            
            if (dimensionUnit1 != null) {
                valueObj1.put("dimensionUnit", dimensionUnit1);
            }
            tupleObj.put("value1", valueObj1);

            JSONObject valueObj2 = new JSONObject();
            valueObj2.put("value", value2);
            valueObj2.put("dimensionName", dimensionName2);
            
            if (dimensionUnit2 != null) {
                valueObj2.put("dimensionUnit", dimensionUnit2);
            }
            tupleObj.put("value2", valueObj2);
        }
        
        contentObj.put("tupleValue", tupleObj);
        jObject.put("content", contentObj);
    }
    
    private void inputThreeDimDataContent(DataExplanationChunk dataExplenationChunk, JSONObject jObject) {
        ThreeDimData threeDimData = (ThreeDimData) dataExplenationChunk.getContent();

        ArrayList<Triple> tripleValues = threeDimData.getValues();

        Dimension dimension1 = threeDimData.getDimension1();
        String dimensionName1 = dimension1.getName();
        String dimensionUnit1 = dimension1.getUnit();

        Dimension dimension2 = threeDimData.getDimension2();
        String dimensionName2 = dimension2.getName();
        String dimensionUnit2 = dimension2.getUnit();

        Dimension dimension3 = threeDimData.getDimension3();
        String dimensionName3 = dimension3.getName();
        String dimensionUnit3 = dimension3.getUnit();

        JSONObject contentObj = new JSONObject();
        JSONObject tripleObj = new JSONObject();
//        Element contentElement = element.addElement("content");
//        Element subContentElement = contentElement.addElement("tripleValue");

        for (Iterator<Triple> it = tripleValues.iterator(); it.hasNext();) {
            Triple triple = it.next();

            String value1 = String.valueOf(triple.getValue1());
            String value2 = String.valueOf(triple.getValue2());
            String value3 = String.valueOf(triple.getValue3());

            JSONObject valueObj1 = new JSONObject();
            valueObj1.put("value", value1);
            valueObj1.put("dimensionName", dimensionName1);
            
            if (dimensionUnit1 != null) {
                valueObj1.put("dimensionUnit", dimensionUnit1);
            }
            tripleObj.put("value1", valueObj1);
            
            JSONObject valueObj2 = new JSONObject();
            valueObj2.put("value", value2);
            valueObj2.put("dimensionName", dimensionName2);
            
            if (dimensionUnit2 != null) {
                valueObj2.put("dimensionUnit", dimensionUnit2);
            }
            tripleObj.put("value2", valueObj2);
            
            
            JSONObject valueObj3 = new JSONObject();
            valueObj3.put("value", value3);
            valueObj3.put("dimensionName", dimensionName3);
            
            if (dimensionUnit3 != null) {
                valueObj3.put("dimensionUnit", dimensionUnit3);
            }
            tripleObj.put("value3", valueObj3);
            
        }
        
        contentObj.put("tripleValue", tripleObj);
        jObject.put("content", contentObj);
    }
}
