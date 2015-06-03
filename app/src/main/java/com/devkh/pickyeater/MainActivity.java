package com.devkh.pickyeater;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class MainActivity extends AppCompatActivity {

    private EntriesManager mEntriesManager = new EntriesManager();
    private String mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText mUserEnteredLocation = (EditText) findViewById(R.id.user_location_entry);
        // user food options entry
        final EditText mUserEntry1 = (EditText) findViewById(R.id.user_entry_1);
        final EditText mUserEntry2 = (EditText) findViewById(R.id.user_entry_2);
        final EditText mUserEntry3 = (EditText) findViewById(R.id.user_entry_3);
        // Button Inflation
        final Button mPickBtn = (Button) findViewById(R.id.pick_btn);
        mPickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPickBtn.setClickable(false); // disable button clicks to prevent spamming clicks

                // can change this also for dynamic entries
                if (!mUserEntry1.getText().toString().isEmpty()) {
                    mEntriesManager.addEntry(mUserEntry1.getText().toString());
                }
                if (!mUserEntry2.getText().toString().isEmpty()) {
                    mEntriesManager.addEntry(mUserEntry2.getText().toString());
                }
                if (!mUserEntry3.getText().toString().isEmpty()) {
                    mEntriesManager.addEntry(mUserEntry3.getText().toString());
                }
                // pass user location & make queries
                if (!mUserEnteredLocation.getText().toString().isEmpty()) {
                    mEntriesManager.setLocation(mUserEnteredLocation.getText().toString());
                }
                AsyncTask<String, Void, String> result = new AsyncTask<String, Void, String>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        makeQueryAndParse();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (!mPickBtn.isClickable()) mPickBtn.setClickable(true);
                        showResult();
                    }
                }.execute();

            }
        });

    }

    private void makeQueryAndParse() {
        final String mSelectedEntry = mEntriesManager.getSelectedEntry();
        final String mLocation = mEntriesManager.getLocation();
        YelpAPI yp = new YelpAPI();
        String resultFromFirstQuery = yp.searchForBusinessesByFood(mSelectedEntry, mLocation);

        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(resultFromFirstQuery);
            System.out.println(response);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(resultFromFirstQuery);
            System.exit(1);
        }

        JSONArray businesses = (JSONArray) response.get("businesses");
        JSONObject firstBusiness = (JSONObject) businesses.get(0);
        String firstBusinessID = firstBusiness.get("id").toString();

        System.out.println(String.format(
                "%s businesses found, querying business info for the top result \"%s\" ...",
                businesses.size(), firstBusinessID));

        // Select the first business and display business details
        mResult = firstBusinessID;//yp.searchByBusinessId(firstBusinessID);
        System.out.println(String.format("Result for business \"%s\" found:", firstBusinessID));
        System.out.println(mResult);
    }

    private void showResult() {
        Intent i = new Intent(this, DisplayResultActivity.class);
        i.putExtra("Result", mResult);
        startActivity(i);
    }


}
