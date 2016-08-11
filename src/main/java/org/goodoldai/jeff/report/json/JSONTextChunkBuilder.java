/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.goodoldai.jeff.report.json;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.json.simple.JSONObject;

/**
 *
 * @author darkostojkovic
 */
public class JSONTextChunkBuilder implements ReportChunkBuilder{
    
    public JSONTextChunkBuilder(){
    
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

        if (!(echunk instanceof TextExplanationChunk)) {
            throw new ExplanationException("The ExplanationChunk must be the type of TextExplanationChunk");
        }

        if (!(stream instanceof JSONObject)) {
            throw new ExplanationException("The stream must be the type of org.json.simple.JSONObject");
        }
        
        JSONObject jObject = (JSONObject) stream;
        
        JSONObject jTextObject = new JSONObject();
        if (insertHeaders)
            JSONChunkUtility.insertExplanationInfo(echunk, jTextObject);

        TextExplanationChunk textExplenationChunk = (TextExplanationChunk) echunk;

        insertContent(textExplenationChunk, jTextObject);
        
        jObject.put("textualExplanation", jTextObject);
    }
    
    private void insertContent(TextExplanationChunk textExplenationChunk, JSONObject jObject) {
        String content = String.valueOf(textExplenationChunk.getContent());
        jObject.put("content", content);
    }
}
