package com.devkh.pickyeater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DisplayResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);
        Intent i = getIntent();

        TextView businessNameView = (TextView) findViewById(R.id.business_name_view);
        TextView businessRatingView = (TextView) findViewById(R.id.business_rating_view);

        String mBusinessName = i.getStringExtra("businessName");
        String mBusinessRating = i.getStringExtra("businessRating");

        businessNameView.setText(mBusinessName.toUpperCase());
        businessRatingView.setText("Rating: " + mBusinessRating);

        // TODO: open with map, open with yelp web

    }
}
