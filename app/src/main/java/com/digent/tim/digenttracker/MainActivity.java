package com.digent.tim.digenttracker;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends BaseMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TheTVDBInterface tvdbInterface = ((TheTVDBInterface) getApplicationContext());
        try {
            tvdbInterface.loginTVDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
