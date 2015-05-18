package com.devkh.pickyeater;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Kevin & Benton on 5/2/15.
 * Manage querying from Yelp
 */

public class QueryManager {

    private String result;
    private Intent i;
    private Context context;
    ResultParser mRP = new ResultParser(); // send result to be parsed

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Make Query with selected user entry
     *
     * @param in       EntriesManager which we will get selected entry
     * @param location location that user entered from field
     */
    public void makeQuery(EntriesManager in, String location) {
        final String entry = in.getSelectedEntry();
        final String userLocation = location;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                result = new YelpAPI().searchForBusinessesByFood(entry, userLocation);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {

                        result = mRP.parseEntryForDesiredResult(result);
                        // reference ResultParser comment for ^
                        return mRP.getBusinessResponseJSON();
                    }

                    // start DisplayResultActivity to display result to user
                    @Override
                    protected void onPostExecute(String s) {
                        i = new Intent(context, DisplayResultActivity.class);
                        i.putExtra("Result", mRP.getFormattedResult());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (null != i) {
                            Toast.makeText(context, "You should eat here!",
                                    Toast.LENGTH_LONG).show();
                            //startActivity shows as null here
                            context.startActivity(i);
                        }
                    }
                }.execute();
            }
        }.execute();

    }
}