package com.digent.tim.digenttracker;

import android.graphics.Bitmap;

import org.json.JSONObject;

/**
 * Created by tim on 15.05.16.
 */
public class SearchResult {

    public JSONObject mSearchResult;
    public JSONObject mGraphicalInformation;
    public JSONObject mActors;
    public Bitmap mBanner;

    public SearchResult(JSONObject searchResult, JSONObject graphicalInformation, JSONObject actors, Bitmap banner) {
        mSearchResult = searchResult;
        mGraphicalInformation = graphicalInformation;
        mActors = actors;
        mBanner = banner;
    }
}
