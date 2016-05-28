package com.digent.tim.digenttracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by tim on 28.05.16.
 */
public class SeriesInformationProvider extends ContentProvider {

    private MainDatabaseHelper mOpenHelper;

    private static final String DATABASE_NAME = "tvdb_cached_data.db";

    private SQLiteDatabase mDatabase;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI("com.digent.tim.digenttracker", "series", 1);
        mUriMatcher.addURI("com.digent.tim.digenttracker", "series/#", 2);
        mUriMatcher.addURI("com.digent.tim.digenttracker", "seriesInformation", 3);
        mUriMatcher.addURI("com.digent.tim.digenttracker", "seriesInformation/#", 4);
    }

    @Override
    public boolean onCreate() {

        mOpenHelper = new MainDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (mUriMatcher.match(uri)) {
            case 1:
                if(TextUtils.isEmpty(sortOrder)) {
                    sortOrder = "_ID ASC";
                }
                break;
            case 2:
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;
            default:
                break;
        }

        mDatabase.query()

        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {

        MainDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TVDBDatabaseContract.SQL_CREATE_SERIES_TABLE);
            db.execSQL(TVDBDatabaseContract.SQL_CREATE_SERIES_INFORMATION_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(TVDBDatabaseContract.SQL_DELETE_SERIES_TABLE);
            db.execSQL(TVDBDatabaseContract.SQL_DELETE_SERIES_INFORMATION_TABLE);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public final class TVDBDatabaseContract {
        public TVDBDatabaseContract() {}

        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_SERIES_TABLE =
                "CREATE TABLE " + SeriesTable.TABLE_NAME + " (" +
                        SeriesTable._ID + " INTEGER PRIMARY KEY," +
                        SeriesTable.COLUMN_NAME_BANNER + TEXT_TYPE + COMMA_SEP +
                        SeriesTable.COLUMN_NAME_FIRSTAIRED + TEXT_TYPE + COMMA_SEP +
                        SeriesTable.COLUMN_NAME_NETWORK + TEXT_TYPE + COMMA_SEP +
                        SeriesTable.COLUMN_NAME_OVERVIEW + TEXT_TYPE + COMMA_SEP +
                        SeriesTable.COLUMN_NAME_SERIESNAME + TEXT_TYPE + COMMA_SEP +
                        SeriesTable.COLUMN_NAME_STATUS + TEXT_TYPE + COMMA_SEP + " )";

        public static final String SQL_CREATE_SERIES_INFORMATION_TABLE =
                "CREATE TABLE " + SeriesInformationTable.TABLE_NAME + " (" +
                        SeriesInformationTable._ID + " INTEGER PRIMARY KEY," +
                        SeriesInformationTable.COLUMN_NAME_SERIESNAME + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_BANNER + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_STATUS + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_FIRSTAIRED + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_NETWORK + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_NETWORKID + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_RUNTIME + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_GENRE + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_OVERVIEW + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_LASTUPDATED  + " INTEGER" + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_AIRSDAYOFWEEK + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_RATING + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_IMDBID + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_ZAP2ITID + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_ADDED + TEXT_TYPE + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_SITERATING + " INTEGER" + COMMA_SEP +
                        SeriesInformationTable.COLUMN_NAME_SITERATINGCOUNT + " INTEGER" + COMMA_SEP +
                        " )";

        public static final String SQL_DELETE_SERIES_INFORMATION_TABLE =
                "DROP TABLE IF EXISTS " + SeriesInformationTable.TABLE_NAME;
        public static final String SQL_DELETE_SERIES_TABLE =
                "DROP TABLE IF EXISTS " + SeriesTable.TABLE_NAME;

        public abstract class SeriesTable implements BaseColumns {
            public static final String TABLE_NAME = "series";
            public static final String COLUMN_NAME_BANNER = "banner";
            public static final String COLUMN_NAME_FIRSTAIRED = "firstAired";
            public static final String COLUMN_NAME_NETWORK = "network";
            public static final String COLUMN_NAME_OVERVIEW = "overview";
            public static final String COLUMN_NAME_SERIESNAME = "seriesName";
            public static final String COLUMN_NAME_STATUS = "status";
        }

        public abstract class SeriesInformationTable implements BaseColumns{
            public static final String TABLE_NAME = "seriesInformation";
            public static final String COLUMN_NAME_SERIESNAME = "seriesName";
            public static final String COLUMN_NAME_BANNER = "banner";
            public static final String COLUMN_NAME_STATUS = "status";
            public static final String COLUMN_NAME_FIRSTAIRED = "firstAired";
            public static final String COLUMN_NAME_NETWORK = "network";
            public static final String COLUMN_NAME_NETWORKID = "networkId";
            public static final String COLUMN_NAME_RUNTIME = "runtime";
            public static final String COLUMN_NAME_GENRE = "genre";
            public static final String COLUMN_NAME_OVERVIEW = "overview";
            public static final String COLUMN_NAME_LASTUPDATED = "lastUpdated";
            public static final String COLUMN_NAME_AIRSDAYOFWEEK = "airsDayOfWeek";
            public static final String COLUMN_NAME_RATING = "rating";
            public static final String COLUMN_NAME_IMDBID = "imdbId";
            public static final String COLUMN_NAME_ZAP2ITID = "zap2itId";
            public static final String COLUMN_NAME_ADDED = "added";
            public static final String COLUMN_NAME_SITERATING = "siteRating";
            public static final String COLUMN_NAME_SITERATINGCOUNT = "siteRatingCount";

        }

    }

}
