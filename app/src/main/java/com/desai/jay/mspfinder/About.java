package com.desai.jay.mspfinder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView aboutdeveloper = (TextView) findViewById(R.id.about12);
        aboutdeveloper.append("About Developer\n\n");
        aboutdeveloper.append("Email :   jay.desai@studentpartner.com");



    }
}
