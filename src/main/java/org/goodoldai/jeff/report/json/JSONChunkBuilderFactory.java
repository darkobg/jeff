/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.goodoldai.jeff.report.json;

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;

/**
 *
 * @author darkostojkovic
 */
public class JSONChunkBuilderFactory implements ReportChunkBuilderFactory{
    private JSONDataChunkBuilder jsonDataChunkBuilder;
    private JSONImageChunkBuilder jsonImageChunkBuilder;
    private JSONTextChunkBuilder jsonTextChunkBuilder;
    
    private JSONChunkBuilderFactory(){
        this.jsonDataChunkBuilder = null;
        this.jsonImageChunkBuilder = null;
        this.jsonTextChunkBuilder = null;
    }

    public ReportChunkBuilder getReportChunkBuilder(ExplanationChunk echunk) {
        if (echunk instanceof TextExplanationChunk) {
            if (jsonTextChunkBuilder == null) {
                jsonTextChunkBuilder = new JSONTextChunkBuilder();
            }
            return jsonTextChunkBuilder;
        }

        if (echunk instanceof ImageExplanationChunk) {
            if (jsonImageChunkBuilder == null) {
                jsonImageChunkBuilder = new JSONImageChunkBuilder();
            }
            return jsonImageChunkBuilder;
        }

        if (echunk instanceof DataExplanationChunk){
            if (jsonDataChunkBuilder == null) {
                jsonDataChunkBuilder = new JSONDataChunkBuilder();
            }
            return jsonDataChunkBuilder;
        }
        
        throw new ExplanationException(
                "The provided ExplanationChunk does not match any of the required types");
    }
}
