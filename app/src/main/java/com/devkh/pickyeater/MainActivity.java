package com.devkh.pickyeater;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
// import android.location.Location;
// import com.google.android.gms.common.ConnectionResult;
// import com.google.android.gms.common.api.GoogleApiClient;
// import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {
    //implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener

    EntriesManager mEntriesManager = new EntriesManager();

    // private GoogleApiClient mGoogleApiClient;
    // protected static final String TAG = "Google API";
    // private Location mUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // we can change this to have IO manager handle this if we want to dynamically add entries
        // user location entry
        final EditText mUserEnteredLocation = (EditText) findViewById(R.id.user_location_entry);
        // user food options entry
        final EditText mUserEntry1 = (EditText) findViewById(R.id.user_entry_1);
        final EditText mUserEntry2 = (EditText) findViewById(R.id.user_entry_2);
        final EditText mUserEntry3 = (EditText) findViewById(R.id.user_entry_3);

        // Get User Location
        // buildGoogleApiClient();

        // Button Inflation
        Button mPickBtn = (Button) findViewById(R.id.pick_btn);
        mPickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                QueryManager mQueryManager = new QueryManager();
                mQueryManager.makeQuery(mEntriesManager, mEntriesManager.getLocation());
                mQueryManager.setContext(getApplicationContext());
            }
        });
    }

    // --------------------------- Activity Life Cycle Control ---------------------------

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mGoogleApiClient.connect();
//    }

    @Override
    protected void onPause() {
        super.onPause();
        mEntriesManager.clearEntries(); // remove all entries when in Paused state
    }

    public void startDisplayResultActivity() {
        Intent i = new Intent("DisplayResultActivity");
        startActivity(i);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
//    }

    // --------------------------- Google API and Location Services ---------------------------

//    /**
//     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
//     */
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }
//
//    @Override
//    public void onConnected(Bundle connectionHint) {
//         /* Provides a simple way of getting a device's location and is well suited for
//          * applications that do not require a fine-grained location and that do not need location
//          * updates. Gets the best and most recent location currently available, which may be null
//          * in rare cases when a location is not available.
//          */
//
//        mUserLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mUserLocation != null) {
//            Log.v("Latitude", String.valueOf(mUserLocation.getLatitude()));
//            Log.v("Longitude", String.valueOf(mUserLocation.getLongitude()));
//        } else {
//            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        // The connection to Google Play services was lost for some reason. We call connect() to
//        // attempt to re-establish the connection.
//        Log.i(TAG, "Connection suspended");
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
//        // onConnectionFailed.
//        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
//                + connectionResult.getErrorCode());
//    }

}
