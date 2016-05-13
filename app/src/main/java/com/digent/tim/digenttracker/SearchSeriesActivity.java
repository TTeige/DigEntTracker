package com.digent.tim.digenttracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

public class SearchSeriesActivity extends AppCompatActivity {

    public JSONArray finalSearchResult = new JSONArray();
    ArrayAdapter<String> adapter;
    String[] seriesNames;
    String[] overview;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_series);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TheTVDBInterface tvdbInterface = ((TheTVDBInterface) getApplicationContext());

        tvdbInterface.searchTVDB(this, message);

    }

    public void setSearchResult(String result) {
        try {
            JSONObject tmp = new JSONObject(result);
            this.finalSearchResult = tmp.getJSONArray("data");
            listView = (ListView) findViewById(R.id.list);
            seriesNames = new String[this.finalSearchResult.length()];
            overview = new String[this.finalSearchResult.length()];

            for (int i = 0; i < this.finalSearchResult.length(); i++) {
                JSONObject series = this.finalSearchResult.getJSONObject(i);
                seriesNames[i] = series.get("seriesName").toString();

                overview[i] = series.get("overview").toString();
            }

            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, seriesNames);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String itemValue = (String) listView.getItemAtPosition(position);

                    Intent intent = new Intent(this, ShowSeriesActivity.class);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelect(MenuItem menuItem) {
        int id = menuItem.getItemId();
        return super.onOptionsItemSelected(menuItem);
    }
}
