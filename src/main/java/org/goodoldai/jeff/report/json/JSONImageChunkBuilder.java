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
 *
 * @author darkostojkovic
 */
public class JSONImageChunkBuilder implements ReportChunkBuilder{
    
    public JSONImageChunkBuilder(){
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
