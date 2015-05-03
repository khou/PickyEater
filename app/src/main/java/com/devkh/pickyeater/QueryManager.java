package com.devkh.pickyeater;

import android.location.Location;
import android.util.Log;



import java.util.Iterator;

/**
 * Created by kevin on 5/2/15.
 * Will manage querying from Yelp
 */

public class QueryManager {

    public void makeQueries(IOManager in, String location) {
        // query for nearby restaurant data from Yelp
        Iterator<String> entriesIterator= in.getEntries();
        while (entriesIterator.hasNext()) {
            Log.v("Query Entries", entriesIterator.next());
            entriesIterator.remove();
        }
    }
}
