package com.digent.tim.digenttracker;

import android.graphics.Bitmap;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by tim on 15.05.16.
 */
public class SearchResult {

    public JSONObject mSearchResult;
    public JSONObject mGraphicalInformation;
    public JSONObject mActors;
    public HashMap<Integer, Bitmap> mBanners;

    public SearchResult(JSONObject searchResult, JSONObject graphicalInformation, JSONObject actors, HashMap<Integer, Bitmap> banners) {
        mSearchResult = searchResult;
        mGraphicalInformation = graphicalInformation;
        mActors = actors;
        mBanners = banners;
    }
}
