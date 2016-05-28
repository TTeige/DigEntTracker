package com.digent.tim.digenttracker;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
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
    private String[] mSeriesNames;
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
        String query = intent.getStringExtra("query");

        setTitle(query);

        TheTVDBInterface tvdbInterface = ((TheTVDBInterface) getApplicationContext());
        String path = "https://api.thetvdb.com/search/series?name=";
        tvdbInterface.searchTVDB(this, path, query);

    }

    @Override
    public void setSearchResult(SearchResult result) {
        try {
            JSONObject tmp = new JSONObject(result.mSearchResult.toString());
            this.mFinalSearchResult = tmp.getJSONArray("data");
            int dataLength = this.mFinalSearchResult.length();

            ListView mListView = (ListView) findViewById(R.id.list);
            mSeriesNames = new String[dataLength];
            final int[] seriesID = new int[dataLength];

            for (int i = 0; i < dataLength; i++) {
                JSONObject series = this.mFinalSearchResult.getJSONObject(i);
                mSeriesNames[i] = series.get("seriesName").toString();
                seriesID[i] = Integer.parseInt(series.get("id").toString());
            }

            ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, mSeriesNames);
            assert mListView != null;
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
}
