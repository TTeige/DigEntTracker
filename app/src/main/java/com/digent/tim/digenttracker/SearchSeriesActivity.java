package com.digent.tim.digenttracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

public class SearchSeriesActivity extends TVDBActivty {

    public JSONArray mFinalSearchResult = new JSONArray();
    ArrayAdapter<String> mAdapter;
    String[] mSeriesNames;
    String[] mOverview;
    ListView mListView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_series);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String message = intent.getStringExtra("query");
        setTitle(message);

        TheTVDBInterface tvdbInterface = ((TheTVDBInterface) getApplicationContext());
        String path = "https://api.thetvdb.com/search/series?name=";
        tvdbInterface.searchTVDB(this, path, message);

    }

    public void setSearchResult(String result) {
        try {
            JSONObject tmp = new JSONObject(result);
            this.mFinalSearchResult = tmp.getJSONArray("data");
            int dataLength = this.mFinalSearchResult.length();

            mListView = (ListView) findViewById(R.id.list);
            mSeriesNames = new String[dataLength];
            mOverview = new String[dataLength];
            final int seriesID[] = new int[dataLength];

            for (int i = 0; i < dataLength; i++) {
                JSONObject series = this.mFinalSearchResult.getJSONObject(i);
                mSeriesNames[i] = series.get("seriesName").toString();

                mOverview[i] = series.get("overview").toString();

                seriesID[i] = Integer.parseInt(series.get("id").toString());
            }

            mAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, mSeriesNames);
            mListView.setAdapter(mAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, ShowSeriesActivity.class);

                    intent.putExtra("seriesID", seriesID[position]);
                    intent.putExtra("seriesName", mSeriesNames[position]);

                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setGraphicalInformation(String result) {

    }


}
