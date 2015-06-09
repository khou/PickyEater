package com.devkh.pickyeater;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class MainActivity extends AppCompatActivity {

    String userLocation;
    String userInput;
    String businessURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText mUserEnteredLocation = (EditText) findViewById(R.id.user_location_entry);
        final EditText mUserEntries = (EditText) findViewById(R.id.user_entry_1);
        final EditText mDistanceEntry = (EditText) findViewById(R.id.distance_entry);

        // Button Inflation
        final Button mPickBtn = (Button) findViewById(R.id.pick_btn);
        mPickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPickBtn.setClickable(false); // disable button clicks to prevent spamming clicks

                if (!mUserEntries.getText().toString().isEmpty() && !mUserEnteredLocation.getText().toString().isEmpty()) {
                    userInput = mUserEntries.getText().toString();
                    userLocation = mUserEnteredLocation.getText().toString();
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
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in both your location and type(s) of food.", Toast.LENGTH_LONG).show();
                    mPickBtn.setClickable(true);
                }
            }
        });
    }

    private void makeQueryAndParse() {

        YelpAPI yp = new YelpAPI();
        String resultFromQuery = yp.searchForFoodByTerm(userInput, userLocation);

        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(resultFromQuery);
            System.out.println(response);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(resultFromQuery);
            System.exit(1);
        }

        JSONArray businesses = (JSONArray) response.get("businesses");
        JSONObject business = (JSONObject) businesses.get(0);
        String businessID = business.get("id").toString();
        businessURL = business.get("url").toString();

        // Console printing for information only
        System.out.println(String.format(
                "%s businesses found, querying business info for the top result \"%s\" ...",
                businesses.size(), businessID));
        System.out.println(String.format("Result for business \"%s\" found:", businessID));
        System.out.println(businessID);
    }

    private void showResult() {
        Intent i = new Intent(this, DisplayResultActivity.class);
        i.putExtra("URL", businessURL);
        startActivity(i);
    }


}
