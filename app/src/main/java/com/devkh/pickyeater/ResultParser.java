package com.devkh.pickyeater;

import org.json.simple.JSONArray;
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
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(entry);
            System.out.println(response);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(entry);
            System.exit(1);
        }
        JSONArray businesses = (JSONArray) response.get("businesses");
        JSONObject firstBusiness = (JSONObject) businesses.get(0);
        String firstBusinessID = firstBusiness.get("id").toString();
        System.out.println(String.format(
                "%s businesses found, querying business info for the top result \"%s\" ...",
                businesses.size(), firstBusinessID));

        // Select the first business and display business details
        String businessResponseJSON = new YelpAPI().searchByBusinessId(firstBusinessID.toString());
        System.out.println(String.format("Result for business \"%s\" found:", firstBusinessID));
        System.out.println(businessResponseJSON);
    }
    // send output to Output Manager
}
