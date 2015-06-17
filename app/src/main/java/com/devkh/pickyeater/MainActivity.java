package com.devkh.pickyeater;

import android.content.Intent;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private String userLocation;
    private String userRadius;
    private String userInputs;
    private String businessURL;
    private String sentResult;

    EditText mUserEnteredLocation;
    EditText mUserEntries;
    EditText mRadiusEntry;
    Button mPickBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button and TextField Inflation
        mUserEnteredLocation = (EditText) findViewById(R.id.user_location_entry);
        mUserEntries = (EditText) findViewById(R.id.user_entry_1);
        mRadiusEntry = (EditText) findViewById(R.id.distance_entry);
        mPickBtn = (Button) findViewById(R.id.pick_btn);

        mPickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPickBtn.setClickable(false); // disable button clicks to prevent spamming clicks

                // These background operations are threaded away from the UI thread
                verifyEntries mVE = new verifyEntries();
                mVE.run();
                parseEntries mPE = new parseEntries();
                mPE.run();

                // Async Task to call the Yelp API
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

    /*
    HELPER METHODS AND CLASSES FOR BACKGROUNDING
     */

    private void makeQueryAndParse() {

        YelpAPI yp = new YelpAPI();
        String resultFromQuery = yp.searchForFoodByTerm(sentResult, userLocation, userRadius);

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

        Random r = new Random();
        JSONArray businesses = (JSONArray) response.get("businesses");
        JSONObject business = (JSONObject) businesses.get(r.nextInt(businesses.size()));
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

    private class verifyEntries implements Runnable {
        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            if (!mUserEntries.getText().toString().isEmpty() && !mUserEnteredLocation.getText().toString().isEmpty()) {
                userInputs = mUserEntries.getText().toString();
                userLocation = mUserEnteredLocation.getText().toString();
                userRadius = mRadiusEntry.getText().toString();
            } else {
                Toast.makeText(getApplicationContext(), "Please fill in both your location and type(s) of food.", Toast.LENGTH_LONG).show();
                mPickBtn.setClickable(true);
            }
        }
    }

    private class parseEntries implements Runnable {
        @Override
        public void run() {
            // Set thread to background priority
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            String[] splitInputs = userInputs.split(",\\s*");
            // Java Random uses timeSeed to randomize
            sentResult = (splitInputs[new Random().nextInt(splitInputs.length)]);
        }
    }
}