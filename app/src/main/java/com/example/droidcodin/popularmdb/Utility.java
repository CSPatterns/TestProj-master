/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.droidcodin.popularmdb;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.example.droidcodin.popularmdb.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

public class Utility {
    public static String getPreferredSortBy(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_order_key),
                context.getString(R.string.pref_sort_order_popular_default));
    }

    public static void storeMovieList(Context context, List<MoviesResponse.Movie> movieList) {
        ArrayList<ContentValues> cvList = new ArrayList<>();
        List<String> favouriteMovieList= getFavouriteMovie(context);
        if(context!=null) {
            for (int i = 0; i < movieList.size(); i++) {
                MoviesResponse.Movie movie = movieList.get(i);
                ContentValues coValues = new ContentValues();

                //get the rating
                Double voteAverage = movie.getVoteAverage();
                coValues.put(MovieContract.MovieEntry.COLUMN_RATING, voteAverage);
                //get the movie data from the JSON response
                //get the title
                String title = movie.getTitle();
                coValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);

                //get the movie release date
                String releaseDate = movie.getReleaseDate();
                coValues.put(MovieContract.MovieEntry.COLUMN_RELEASEDATE, releaseDate);

                //get the description of the movie
                String description = movie.getDescription();
                coValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, description);

                String movieId = movie.getMovie_id();
                coValues.put(MovieContract.MovieEntry.COLUMN_ID, movieId);

                //get the poster url
                String posterPath = movie.getPoster();
                coValues.put(MovieContract.MovieEntry.COLUMN_IMAGE, posterPath);


//                String favouriteMovie = movie.g();
//                coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORED, favouriteMovie);

                //get the total number of votes
//                int totalVotes = movie.getMovieVoteCount();
//                coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, totalVotes);

                //coValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE_MOVIE, typeMovie);
                if (!favouriteMovieList.contains((movieId))) {
                    cvList.add(coValues);
                }
            }
            //insert into the DB
            ContentValues[] values = new ContentValues[cvList.size()];
            cvList.toArray(values);

            int itemsAdded = context.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, values);

        }
    }

    public static List<String> getFavouriteMovie(Context context) {
        List<String> favouriteMovie = new ArrayList<>();
        if(context!=null) {
            Cursor c = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    new String[]{MovieContract.MovieEntry.COLUMN_ID},
                    null, null,
                    null);
            if (c.moveToFirst()) {
                do {
                    int s = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID);
                    favouriteMovie.add(c.getString(s));
                } while (c.moveToNext());
            }
            c.close();
        }
        return favouriteMovie;
    }

//    public static boolean isMetric(Context context) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        return prefs.getString(context.getString(R.string.pref_units_key),
//                context.getString(R.string.pref_units_metric))
//                .equals(context.getString(R.string.pref_units_metric));
//    }
//
//    static String formatTemperature(double temperature, boolean isMetric) {
//        double temp;
//        if ( !isMetric ) {
//            temp = 9*temperature/5+32;
//        } else {
//            temp = temperature;
//        }
//        return String.format("%.0f", temp);
//    }
//
//    static String formatDate(long dateInMillis) {
//        Date date = new Date(dateInMillis);
//        return DateFormat.getDateInstance().format(date);
//    }
}