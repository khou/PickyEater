package com.devkh.pickyeater;

import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Kevin & Benton on 5/2/15.
 * Will manage querying from Yelp
 */

public class QueryManager extends AsyncTask<String, Void, String> {

    YelpAPI yelp = new YelpAPI();
    private ArrayList<String> resultList = new ArrayList<>();

    // WHILE LOOP MAY BE BAD - Not sure if we want to start a
    // doInBackground for every single entry. Maybe we can
    // start one doInBackground and search them all at once? idk.
    public void makeQueries(InputManager in, String location) {
        // query for nearby restaurant data from Yelp
        Iterator<String> entriesIterator = in.getEntries();
        while (entriesIterator.hasNext()) {
            // use doInBackground to call Yelp
            String result = doInBackground(entriesIterator.next(), location);
            resultList.add(result);
            entriesIterator.remove();
        }
    }

    // @params : params[0] = entry term & params[1] = location
    @Override
    protected String doInBackground(String... params) {
        String term = params[0];
        String location = params[1];
        String searchResult = yelp.searchByFood(term, location);
        return searchResult;
    }
    // Do something in onPostExecute to send data back?
    // Send to Parser

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        new ResultParser().retrieveResults();
    }
}
