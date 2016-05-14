package com.digent.tim.digenttracker;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowSeriesActivity extends TVDBActivty {

    JSONObject mSeriesInformation;
    JSONObject mGraphicalInformation;
    TheTVDBInterface mTvdbInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_series);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        int seriesID = getIntent().getExtras().getInt("seriesID");
        String seriesName = getIntent().getExtras().getString("seriesName");

        setTitle(seriesName);

        mTvdbInterface = ((TheTVDBInterface) getApplicationContext());

        String path = "https://api.thetvdb.com/series/";
        mTvdbInterface.searchTVDB(this, path, String.valueOf(seriesID));
    }

    @Override
    public void setSearchResult(String result) {
        try {
            mSeriesInformation = new JSONObject(result);
            mSeriesInformation = mSeriesInformation.getJSONObject("data");
            mTvdbInterface.getGraphicalInformation(this, mSeriesInformation.getString("id"), "poster", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setGraphicalInformation(String result) {
        try {
            mGraphicalInformation = new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
