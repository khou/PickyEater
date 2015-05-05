package com.devkh.pickyeater;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Kevin & Benton on 5/2/15.
 * Will manage querying from Yelp
 */

public class QueryManager {

    YelpAPI yelp = new YelpAPI();
    private ArrayList<String> resultList = new ArrayList<>();

    public void makeQuery(InputManager in, String loc) {
        final Iterator entriesIterator = in.getEntries();
        final String location = loc;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                while (entriesIterator.hasNext()) {
                    String searchResult = yelp.searchByFood(entriesIterator.next().toString(), location);
                    resultList.add(searchResult);
                    entriesIterator.remove();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                new ResultParser().parseResults(resultList);
            }
        }.execute();

    }

    // @params : params[0] = entry term & params[1] = location
//    @Override
//    protected String doInBackground(String... params) {
//        String term = params[0];
//        String location = params[1];
//        String searchResult = yelp.searchByFood(term, location);
//        return searchResult;
//    }
}