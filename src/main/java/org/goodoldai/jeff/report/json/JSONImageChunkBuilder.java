/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.goodoldai.jeff.report.json;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.json.simple.JSONObject;

/**
 * A concrete builder for transforming image explanation chunks into pieces 
 * of XML report
 * 
 * @author darkostojkovic
 */
public class JSONImageChunkBuilder implements ReportChunkBuilder{
    
    /**
     * Public builder initialization
     */
    public JSONImageChunkBuilder(){
    }
    
    /**
     * This method transforms an image explanation chunk into an JSON report piece
     * and writes this piece into the provided JSON object which is, in this
     * case, an instance of org.json.simple.JSONObject. The method first collects
     * all general chunk data (context, rule, group, tags) and inserts them 
     * into the report, and then retrieves the chunk content. Since the content 
     * is, in this case, an ImageData instance, and since images cannot be 
     * displayed in JSON objects, as attributes, only the image data (caption and URL) gets 
     * inserted into the report. If the image caption is missing, it doesn't 
     * get inserted into the report.
     *
     * @param echunk image explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be
     * written in as an JSON object attributes(in this case org.json.simple.JSONObject)
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null, if the entered chunk is not an ImageExplanationChunk instance or 
     * if the entered output stream type is not org.json.simple.JSONObject
     */
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

        if (!(echunk instanceof ImageExplanationChunk)) {
            throw new ExplanationException("The ExplanationChunk must be type of ImageExplanationChunk");
        }

        if (!(stream instanceof JSONObject)) {
            throw new ExplanationException("The stream must be the type of org.json.simple.JSONObject");
        }
        
        JSONObject jObject = (JSONObject) stream;
        
        JSONObject jsonImageExp = new JSONObject();
        
        if (insertHeaders)
            JSONChunkUtility.insertExplanationInfo(echunk, jsonImageExp);
        
        ImageExplanationChunk imageExplanationChunk = (ImageExplanationChunk) echunk;
        insertContent((ImageData) imageExplanationChunk.getContent(), jsonImageExp);
        
        jObject.put("imageExplanation", jsonImageExp);
    }
    
    /**
     * This is a private method that is used to insert content into the JSON object
     *
     * @param imageExplanationChunk image explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an JSON object attributes(in this case org.json.simple.JSONObject)
     */
    private void insertContent(ImageData imageData, JSONObject jObject) {

        String url = imageData.getURL();
        String caption = imageData.getCaption();

        JSONObject jContentObj = new JSONObject();
        jContentObj.put("imageUrl", url);
        if(caption != null)
        {
            jContentObj.put("caption", caption);
        }
        jObject.put("content", jContentObj);
    }
}
