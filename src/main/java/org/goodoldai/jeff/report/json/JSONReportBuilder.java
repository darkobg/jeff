/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.goodoldai.jeff.report.json;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.report.ReportBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;
import org.json.simple.JSONObject;

/**
 *
 * @author darkostojkovic
 */
public class JSONReportBuilder extends ReportBuilder {

    public JSONReportBuilder(ReportChunkBuilderFactory factory) {
        super(factory);
    }

    @Override
    public void buildReport(Explanation explanation, String filepath) {
        if (explanation == null) {
            throw new ExplanationException("The entered explanation must not be null");
        }

        if (filepath == null || filepath.isEmpty()) {
            throw new ExplanationException("The entered filepath must not be null or empty string");
        }

        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new File(filepath));

            buildReport(explanation, writer);

        } catch (IOException ex) {
            throw new ExplanationException(
                    "The file could not be writen due to fallowing IO error: " + ex.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    protected void insertHeader(Explanation explanation, Object stream) {
        if (explanation == null && stream == null) {
            throw new ExplanationException("All of the arguments are mandatory, so they can not be null");
        }

        if (explanation == null) {
            throw new ExplanationException("The argument 'explanation' is mandatory, so it can not be null");
        }

        if (stream == null) {
            throw new ExplanationException("The argument 'stream' is mandatory, so it can not be null");
        }

        if (!(stream instanceof JSONObject)) {
            throw new ExplanationException("The argument 'stream' must be the type of org.dom4j.Document");
        }

        JSONObject jObject = (JSONObject) stream;

        String date = DateFormat.getInstance().format(explanation.getCreated().getTime());
        String owner = explanation.getOwner();
        String language = explanation.getLanguage();
        String country = explanation.getCountry();
        String title = explanation.getTitle();
        
        jObject.put("date", date);
        jObject.put("owner", owner);
        jObject.put("language", language);
        jObject.put("country", country);
        jObject.put("title", title);
    }
    
}
