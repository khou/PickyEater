package com.devkh.pickyeater;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by kevin on 5/2/15.
 * Manges parsing of JSON results
 */
public class ResultParser {

    public void parseEntryForDesiredResult(String entry) {
        // parse results
        JSONParser parser = new JSONParser();
        JSONObject response;
        try {
            response = (JSONObject) parser.parse(entry);
            System.out.println(response);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(entry);
            System.exit(1);
        }
    }
    // send output to Output Manager
}
