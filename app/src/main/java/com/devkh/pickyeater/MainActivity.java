package com.devkh.pickyeater;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    // TODO: automatically get user location
    // TODO: catch invalid (blank) user entry that can crash app

    private String userInputs;
    private String businessURL;
    private String sentResult;
    private WebView mDisplayResult;

    EditText mUserEntries;
    Button mPickBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button and TextField Inflation
        mUserEntries = (EditText) findViewById(R.id.user_entry_1);
        mPickBtn = (Button) findViewById(R.id.pick_btn);
        mDisplayResult = (WebView) findViewById(R.id.web_view);


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
                new AsyncTask<String, Void, String>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        mDisplayResult.setWebViewClient(new WebViewClient());
                        WebSettings webSettings = mDisplayResult.getSettings();
                        webSettings.setJavaScriptEnabled(true);
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
     * TODO: If installed, open with Yelp. If not, open new activity with button to download since WebView sucks.
     *
     */
    private void showResult() {
        if (isPackageExisted("com.yelp.android")) {
            Log.v("Yelp Installed", "true");

        } else {
            Log.v("Yelp Installed", "false");
            mDisplayResult.loadUrl(businessURL);
            mDisplayResult.setVisibility(View.VISIBLE);
        }
    }

    /*
     *  HELPER METHODS AND CLASSES FOR BACKGROUNDING
     */

    private void makeQueryAndParse() {

        YelpAPI yp = new YelpAPI();
        String resultFromQuery = yp.searchForFoodByTerm(sentResult);

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

    private boolean isPackageExisted(String targetPackage) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    private class verifyEntries implements Runnable {
        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            if (!mUserEntries.getText().toString().isEmpty()) {
                userInputs = mUserEntries.getText().toString();
            } else {
                Toast.makeText(getApplicationContext(), "Please fill in type(s) of food.", Toast.LENGTH_LONG).show();
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