package com.devkh.pickyeater;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    // TODO: automatically get user location

    // TODO: powered by Yelp logo
    
    private String mUserInputs;
    private String mBusinessURL;
    private String mSentResults;
    private String mBusinessName;
    private String mBusinessRating;
    MultiAutoCompleteTextView mUserEntries;
    Button mPickBtn;
    Toast mToast;

    // TODO: autocomplete from web instead
    private static final String[] FOOD_TYPES = new String[]{
            "sushi", "pizza", "burrito", "burger", "fries"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Button and TextField Inflation
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, FOOD_TYPES);
        mUserEntries = (MultiAutoCompleteTextView) findViewById(R.id.user_entry);
        mPickBtn = (Button) findViewById(R.id.pick_btn);

        mUserEntries.setAdapter(adapter);
        mUserEntries.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        mPickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPickBtn.setClickable(false); // disable button clicks to prevent spamming clicks
                if (mUserEntries.getText().toString().isEmpty()) {
                    mToast = Toast.makeText(getApplicationContext(), "Using Default", Toast.LENGTH_SHORT);
                    mToast.show();
                    mUserInputs = "sushi, pizza, burrito";
                } else {
                    mUserInputs = mUserEntries.getText().toString();
                    mToast = Toast.makeText(getApplicationContext(), "Picking...", Toast.LENGTH_SHORT);
                    mToast.show();
                }

                // These background operations are threaded away from the UI thread
                parseEntries mPE = new parseEntries();
                mPE.run();

                // Async Task to call the Yelp API
                new AsyncTask<String, Void, String>() {

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
     * Show results of query
     * TODO: Open results with Yelp... Currently only opening Yelp
     */
    private void showResult() {
        mToast.setText("Try this!");
        mToast.show();
        if (isPackageExisted("com.yelp.android")) {
            Log.v("Yelp Installed", "true");
            // Toast.makeText(getApplicationContext(), "Opening with Yelp", Toast.LENGTH_SHORT).show();
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.yelp.android");
            startActivity(launchIntent);
        } else {
            Log.v("Yelp Installed", "false");

            Intent i = new Intent(this, DisplayResultActivity.class);
            i.putExtra("businessURL", mBusinessURL);
            i.putExtra("businessName", mBusinessName);
            i.putExtra("businessRating", mBusinessRating);
            startActivity(i);
        }
    }

    /*
     *  HELPER METHODS AND CLASSES FOR BACKGROUND
     */

    private void makeQueryAndParse() {

        YelpAPI yp = new YelpAPI();
        String resultFromQuery = yp.searchForFoodByTerm(mSentResults);

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
        mBusinessURL = business.get("url").toString();
        mBusinessName = business.get("name").toString();
        mBusinessRating = business.get("rating").toString();

        // Console printing for information only
        System.out.println(String.format(
                "%s businesses found, querying business info for the top result \"%s\" ...",
                businesses.size(), businessID));
        System.out.println(String.format("Result for business \"%s\" found:", businessID));
        System.out.println(businessID);
    }

    private boolean isPackageExisted(String targetPackage) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    private class parseEntries implements Runnable {
        @Override
        public void run() {
            // Set thread to background priority
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            String[] splitInputs = mUserInputs.split(",\\s*");
            // Java Random uses timeSeed to randomize
            mSentResults = (splitInputs[new Random().nextInt(splitInputs.length)]);
        }
    }
}