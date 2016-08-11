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
 *
 * @author darkostojkovic
 */
public class JSONChunkUtility {
    
    private JSONChunkUtility(){
    }
    
    
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
