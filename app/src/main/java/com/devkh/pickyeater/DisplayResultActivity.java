package com.devkh.pickyeater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DisplayResultActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);
        Intent i = getIntent();

        String businessURL = i.getStringExtra("URL");
        WebView mDisplayResult = (WebView) findViewById(R.id.webView1);
        mDisplayResult.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mDisplayResult.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mDisplayResult.loadUrl(businessURL);
    }
}
