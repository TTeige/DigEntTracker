package com.digent.tim.digenttracker;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowSeriesActivity extends TVDBActivty {

    private int seriesID;
    private String seriesName;

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

        seriesID = getIntent().getExtras().getInt("seriesID");
        seriesName = getIntent().getExtras().getString("seriesName");

        setTitle(seriesName);

        TheTVDBInterface mTvdbInterface = ((TheTVDBInterface) getApplicationContext());
        String path = "https://api.thetvdb.com/series/";
        mTvdbInterface.seriesSearchTVDB(this, path, String.valueOf(seriesID));
    }

    public void launchActorIntent(View view) {
        Intent intent = new Intent(this, ActorActivity.class);
        intent.putExtra("seriesID", seriesID);
        intent.putExtra("seriesName", seriesName);
        startActivity(intent);
    }

    @Override
    public void setSearchResult(SearchResult result) {
        try {
            TextView ratingView = (TextView) findViewById(R.id.rating);
            if (ratingView != null) {
                Resources res = getResources();
                String ratingText = String.format(res.getString(R.string.tvdb_rating), result.mSearchResult.getString("siteRating"));
                ratingView.setText(ratingText);
            }

            TextView overviewText = (TextView) findViewById(R.id.overview);
            if (overviewText != null) {
                overviewText.setText(result.mSearchResult.getString("overview"));
            }

            ImageView imageView = (ImageView) findViewById(R.id.banner_image);
            if (imageView != null) {
                imageView.setImageBitmap(result.mBanner);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
