package com.digent.tim.digenttracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ActorActivity extends TVDBActivty {

    String mFullSeriesPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String seriesName = intent.getStringExtra("seriesName");
        int seriesID = intent.getIntExtra("seriesID", 0);

        setTitle(seriesName);

        TheTVDBInterface mTvdbInterface = ((TheTVDBInterface) getApplicationContext());
        String path = "https://api.thetvdb.com/series/";
        mFullSeriesPath = path + String.valueOf(seriesID);
        mTvdbInterface.actorSearchTVDB(this, path, String.valueOf(seriesID));
    }

    @Override
    public void setSearchResult(SearchResult result) {
        try {
            ListView listView = (ListView) findViewById(R.id.list);
            JSONArray actorArray = result.mActors.getJSONArray("data");
            int dataLength = actorArray.length();
            ArrayList<HashMap<String, String>> actorList = new ArrayList<>();
            for (int i = 0; i < dataLength; i++) {
                HashMap<String, String> actor = new HashMap<>();
                JSONObject JSONactor = actorArray.getJSONObject(i);
                actor.put("title", JSONactor.getString("name"));
                actor.put("subtext", JSONactor.getString("role"));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                result.mBanners.get(JSONactor.getInt("id")).compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();
                String encoded = Base64.encodeToString(b, Base64.DEFAULT);
                actor.put("bitmap", encoded);
                actorList.add(actor);
            }

            if(!actorList.isEmpty()) {
                Collections.sort(actorList, new MapComparator("title"));

                CustomImageListAdapter mAdapter = new CustomImageListAdapter(this, actorList);
                if (listView != null) {
                    listView.setAdapter(mAdapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class MapComparator implements Comparator<Map<String, String>> {

        private final String key;

        public MapComparator(String key)
        {
            this.key = key;
        }


        @Override
        public int compare(Map<String, String> lhs, Map<String, String> rhs) {
            return lhs.get(key).compareTo(rhs.get(key));
        }
    }
}
