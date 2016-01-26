package com.example.droidcodin.popularmdb.data;

/**
 * Created by Archie David on 28/12/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.droidcodin.popularmdb.data.MovieContract.MovieEntry;
import com.example.droidcodin.popularmdb.data.MovieContract.ReviewEntry;
import com.example.droidcodin.popularmdb.data.MovieContract.VideoEntry;
/**
 * Manages a local database for weather data.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MovieDbHelper.class.getSimpleName();

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 27;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + VideoEntry.TABLE_NAME + " (" +
                VideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VideoEntry.COLUMN_ID + " INTEGER, " +
                VideoEntry.COLUMN_LANGUAGE + " TEXT, " +
                VideoEntry.COLUMN_KEY + " TEXT, " +
                VideoEntry.COLUMN_NAME + " TEXT, " +
                VideoEntry.COLUMN_SITE + " TEXT, " +
                VideoEntry.COLUMN_TYPE + " TEXT, " +
                "UNIQUE (" + VideoEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ReviewEntry.COLUMN_ID + " INTEGER, " +
                ReviewEntry.COLUMN_AUTHOR + " TEXT, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT, " +
                "UNIQUE (" + ReviewEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";


        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_ID + " INTEGER, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_IMAGE + " TEXT, " +
                MovieEntry.COLUMN_IMAGE2 + " TEXT, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_RELEASEDATE + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_FAVFLAG + " INTEGER NOT NULL DEFAULT 0, " +
                MovieEntry.COLUMN_RATING + " TEXT, " +
                "UNIQUE (" + MovieEntry.COLUMN_ID + ") ON CONFLICT REPLACE, " +

                // To assure the application have just one movie entry per title
                // per release date, create a UNIQUE constraint with REPLACE strategy
                //this way no two movies can have the exact same title and releasedate
                "UNIQUE" + "(" + MovieEntry.COLUMN_TITLE + "," +  MovieContract.MovieEntry.COLUMN_RELEASEDATE + ") ON CONFLICT REPLACE);";


        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_VIDEO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");

        // Drop the Movie table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MovieContract.MovieEntry.TABLE_NAME + "'");

        // Drop the Movie Review table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MovieContract.ReviewEntry.TABLE_NAME + "'");

        // Drop the Video table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.VideoEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MovieContract.VideoEntry.TABLE_NAME + "'");


        // re-create database
        onCreate(sqLiteDatabase);
    }
}