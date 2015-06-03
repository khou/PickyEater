package com.devkh.pickyeater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by kevin on 5/7/15.
 */

public class DisplayResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_result_activity);
        Intent i = getIntent();
        TextView mTextView = (TextView) findViewById(R.id.result_text_view);
        mTextView.setText(i.getStringExtra("Result"));
    }
}
