package com.devkh.pickyeater;

import android.os.AsyncTask;

/**
 * Created by Kevin & Benton on 5/2/15.
 * Manage querying from Yelp
 */

public class QueryManager {

    private String result;

    /**
     * Make Query with selected user entry
     * @param in EntriesManager which we will get selected entry
     * @param location location that user entered from field
     */
    public void makeQuery(EntriesManager in, String location) {
        final String entry = in.getSelectedEntry();
        final String userLocation = location;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                result = new YelpAPI().searchForBusinessesByFood(entry, userLocation);
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                new ResultParser().parseEntryForDesiredResult(result); // send result to be parsed
            }
        }.execute();

    }
}