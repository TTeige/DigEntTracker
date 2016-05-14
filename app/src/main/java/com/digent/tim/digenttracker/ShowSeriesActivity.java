package com.digent.tim.digenttracker;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

public class ShowSeriesActivity extends TVDBActivty {

    SearchResult mSearchResult;
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
        mTvdbInterface.seriesSearchTVDB(this, path, String.valueOf(seriesID));
    }

    @Override
    public void setSearchResult(SearchResult result) {
        try {
            mSearchResult = result;
            TextView overviewText = (TextView) findViewById(R.id.overview);
            if (overviewText != null) {
                overviewText.setText(mSearchResult.mSearchResult.getString("overview"));
            }

            ImageView imageView = (ImageView) findViewById(R.id.banner_image);
            if (imageView != null) {
                imageView.setImageBitmap(mSearchResult.mBanner);
            }

            ListView listView = (ListView) findViewById(R.id.list);
            JSONArray actorArray = mSearchResult.mActors.getJSONArray("data");
            int dataLength = actorArray.length();
            String[] actorNames = new String[dataLength];
            String[] roleNames = new String[dataLength];
            for (int i = 0; i < dataLength; i++) {
                actorNames[i] = actorArray.getJSONObject(i).getString("name");
                roleNames[i] = actorArray.getJSONObject(i).getString("role");
            }
            ArrayAdapter mAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, roleNames);
            if (listView != null) {
                listView.setAdapter(mAdapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
