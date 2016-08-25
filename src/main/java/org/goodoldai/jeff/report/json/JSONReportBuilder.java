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
 * A concrete ReportBuilder implementation. This class is used when the  
 * output report is supposed to be JSON Object.
 * 
 * @author darkostojkovic
 */
public class JSONReportBuilder extends ReportBuilder {

    /**
     * Just calls the superclass constructor.
     *
     * @param factory chunk builder factory
     */
    public JSONReportBuilder(ReportChunkBuilderFactory factory) {
        super(factory);
    }

    /**
     * Creates an JSON report based on the provided explanation and sends it to 
     * an .json file as output. If the file doesn't exist, it is created. If it 
     * exists, it is overwritten.
     * 
     * Basically, this method opens the output stream and
     * it creates PrintWriter (based on the provided file path)
     * which holds all of the explanation information before it is put into the stream,
     * and calls the "buildReport" method that has an JSONObject an argument.
     *
     * @param explanation the explanation that needs to be transformed into a
     * report
     * @param filepath a string representing an URL for the file
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are null,
     * if filepath is an empty string, or if IOException is caught
     */
    @Override
    public void buildReport(Explanation explanation, String filepath) {
        if (explanation == null) {
            throw new ExplanationException("The entered explanation must not be null");
        }

        if (filepath == null || filepath.isEmpty()) {
            throw new ExplanationException("The entered filepath must not be null or empty string");
        }

        PrintWriter writer = null;
        JSONObject jObject = null;
        try {
            writer = new PrintWriter(new File(filepath));
            jObject = new JSONObject();
            
            buildReport(explanation, jObject);
            
            writer.write(jObject.toJSONString());
        } catch (IOException ex) {
            throw new ExplanationException(
                    "The file could not be writen due to fallowing IO error: " + ex.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Creates a report based on the provided explanation and writes it to the
     * provided object that is type of org.json.simple.JSONObject before it is written in
     * the file 
     *
     * @param explanation the explanation that needs to be transformed into a
     * report
     * @param stream output stream to which the report is to be written
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are null
     */
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
        
        if(date != null && !date.isEmpty())
        {
            jObject.put("date", date);
        }
        if(owner != null && !owner.isEmpty())
        {
            jObject.put("owner", owner);
        }
        if(language != null && !language.isEmpty())
        {
            jObject.put("language", language);
        }
        if(country != null && !country.isEmpty())
        {
            jObject.put("country", country);
        }
        if(title != null && !title.isEmpty())
        {
            jObject.put("title", title);
        }
    }
    
}
