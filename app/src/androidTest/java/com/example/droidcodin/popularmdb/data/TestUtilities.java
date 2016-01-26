package com.example.droidcodin.popularmdb.data;

/**
 * Created by Archie David on 28/12/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.example.droidcodin.popularmdb.utils.PollingCheck;

import java.util.Map;
import java.util.Set;


/*
    These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your MovieContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {
   // static final String TEST_MOVIE_ID = "99705";
    //static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
         Use this to create some default movie values for your database tests.
     */
    static ContentValues createMovieItemValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry._ID, 1);
        testValues.put(MovieContract.MovieEntry.COLUMN_ID, 2);
        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Ant Man");
        testValues.put(MovieContract.MovieEntry.COLUMN_IMAGE, "testimage");
        testValues.put(MovieContract.MovieEntry.COLUMN_IMAGE2, "testimage2");
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "summary test");
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASEDATE, 10);
        testValues.put(MovieContract.MovieEntry.COLUMN_FAVFLAG, 1);
        testValues.put(MovieContract.MovieEntry.COLUMN_RATING, "2016-10-01");


        return testValues;
    }



    /*
         test insert Movie values in the Movie table
     */
    static long insertMovieItemValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieItemValues();

        long movieRowId;
        movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", movieRowId != -1);

        return movieRowId;
    }

    /*
        Use id 157336 (Insterstellar movie_id) to create Review values for Insterstellar
    */
    static ContentValues createInterstellarMovieReviewValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.ReviewEntry.COLUMN_ID, 12);
        testValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, "BenSmith");
        testValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, "Testcontent");

        return testValues;
    }

    /*
        test insert Review values in the Review table
     */
    static long insertinterstellarMovieReviewValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createInterstellarMovieReviewValues();

        long reviewRowId;
        reviewRowId = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Interstellar Movie Review values", reviewRowId != -1);

        return reviewRowId;
    }

    /*
    Use id 157336 (Insterstellar movie_id) to create Video values for Insterstellar
   */
    static ContentValues createInterStellarMovieVideoValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.VideoEntry.COLUMN_ID, 157336);
        testValues.put(MovieContract.VideoEntry.COLUMN_LANGUAGE, "en");
        testValues.put(MovieContract.VideoEntry.COLUMN_KEY, "64BADF7488");
        testValues.put(MovieContract.VideoEntry.COLUMN_NAME, "AANDFD");
        testValues.put(MovieContract.VideoEntry.COLUMN_SITE, "FDFLDASK;F");
        testValues.put(MovieContract.VideoEntry.COLUMN_SIZE, "123");
        testValues.put(MovieContract.VideoEntry.COLUMN_TYPE, "ABCTYPE");


        return testValues;
    }

    /*
        Students: You can uncomment this function once you have finished creating the
        LocationEntry part of the WeatherContract as well as the WeatherDbHelper.
     */
    static long insertInterStellarMovieVideoValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createInterStellarMovieVideoValues();

        long videoRowId;
        videoRowId = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", videoRowId != -1);

        return videoRowId;
    }


    /*
        The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}