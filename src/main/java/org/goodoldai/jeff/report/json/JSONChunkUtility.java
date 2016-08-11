/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.goodoldai.jeff.report.json;

import java.lang.reflect.Field;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * This class is used to do the utility jobs that are common for JSON report
 * generation purposes.
 *
 * @author darkostojkovic
 */
public class JSONChunkUtility {
    
    /**
     * Constructor should be private to prevent initialization
     */
    private JSONChunkUtility(){
    }
    
    /**
     * This method inserts the general explanation information (context, rule, group, tags)
     * into the main JSON report object
     *
     * @param echunk explanation chunk that holds the information that needs to be inserted
     * @param element in which the content of the transformed chunk will be
     * written in as JSON object attributes 
     */
    static void insertExplanationInfo(ExplanationChunk echunk, JSONObject jObject) {

        int cont = echunk.getContext();
        String context = translateContext(cont, echunk);

        String rule = echunk.getRule();
        String group = echunk.getGroup();
        String[] tags = echunk.getTags();

        if (rule != null) {
            jObject.put("rule", rule);
        }
        
        if (group != null) {
            jObject.put("group", group);
        }

        jObject.put("context", context);

        if (tags != null) {
            JSONArray jArray = new JSONArray();
            for (int i = 0; i < tags.length; i++) {
                jArray.add(new JSONObject().put("tag", tags[i]));
            }
            jObject.put("tags", jArray);
        }
    }
    
    
    /**
     *  This is a method that is used to translate the context from an integer
     * into a String. This method uses reflection in order to do this.
     *
     * @param context the int representation of explanation context
     * @param echunk explanation chunk that holds the string value of the context
     *
     * @return the string value of the context
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException
     * In the case of any problems by covering raised IllegalArgumentException or
     * IllegalAccessException.
     */
    static String translateContext(int context, ExplanationChunk echunk) {

        Class cl = echunk.getClass();
        Field fields[] = cl.getFields();

        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];
                if (field.getInt(field.getName()) == context) {
                    return field.getName().toLowerCase();
                }
            } catch (IllegalArgumentException ex) {
                throw new ExplanationException(ex.getMessage());
            } catch (IllegalAccessException ex) {
                throw new ExplanationException(ex.getMessage());
            }
        }
        return String.valueOf(context);
    }
}
