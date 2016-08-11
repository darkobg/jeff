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
 * A concrete implementation of the ReportChunkBuilderFactory interface for
 * reports that are supposed to be JSON documents. This class provides 
 * references to the concrete report chunk builder instances for the JSON 
 * report type.
 * 
 * @author darkostojkovic
 */
public class JSONChunkBuilderFactory implements ReportChunkBuilderFactory{
    /**
     * An JSONDataChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private JSONDataChunkBuilder jsonDataChunkBuilder;
    /**
     * An JSONImageChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private JSONImageChunkBuilder jsonImageChunkBuilder;
    /**
     * An JSONTextChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private JSONTextChunkBuilder jsonTextChunkBuilder;
    
    /**
     * Initializes all attributes (chunk builder references) to null.
     */
    private JSONChunkBuilderFactory(){
        this.jsonDataChunkBuilder = null;
        this.jsonImageChunkBuilder = null;
        this.jsonTextChunkBuilder = null;
    }

    /**
     * This method returns the appropriate chunk builder instances for the
     * entered explanation chunk. If, for example, a DataExplanationChunk was 
     * entered as an argument, the method would return an JSONDataChunkBuilder 
     * instance.
     *
     * It is necessary to state that chunk builder instances are
     * "lazy initialized" and cached (as attributes) while the factory instance
     * exists. This means that, for example, JSONImageChunkBuilder attribute is 
     * null when the factory is created and initialized only when the first
     * JSONImageChunkBuilder instance is needed and not before. In all
     * subsequent calls when this method is supposed to return a
     * JSONImageChunkBuilder instance it returns the reference to the already
     * initialized JSONImageChunkBuilder object.
     *
     * @param echunk explanation chunk that needs to be transformed
     * into a report piece
     * 
     * @return chunk builder instance that is supposed to be used in order
     * to transform the entered chunk
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the type was not recognized
     */
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
