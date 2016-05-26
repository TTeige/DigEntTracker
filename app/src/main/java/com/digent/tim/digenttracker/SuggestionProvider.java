package com.digent.tim.digenttracker;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by tim on 15.05.16.
 */
public class SuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.digent.tim.digenttracker.SuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}

