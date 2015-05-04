package com.devkh.pickyeater;

import android.location.Location;
import android.util.Log;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Kevin & Benton on 5/2/15.
 * Will manage querying from Yelp
 */

public class QueryManager {

    public void makeQueries(IOManager in, String location) {
        // query for nearby restaurant data from Yelp
        YelpAPI yelp = new YelpAPI();
        ArrayList<String> resultList = new ArrayList<>();

        // iterate through entries
        Iterator<String> entriesIterator = in.getEntries();
        while (entriesIterator.hasNext()) {
            // call to Yelp to search and add to resultList
            String result = yelp.searchByFood(entriesIterator.next(), location);
            resultList.add(result);
            entriesIterator.remove();
        }


    }


}
