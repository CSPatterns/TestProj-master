package com.example.droidcodin.popularmdb;

import retrofit.RestAdapter;

/**
 * Created by Archie David on 20/01/16.
 */
public class RestClient {
    private static final String BASE_URL = "http://api.themoviedb.org";
    private MovieApiService movieApiService;


    public RestClient() {


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .build();

        movieApiService = restAdapter.create(MovieApiService.class);
    }

    public MovieApiService getApiService() {
        return movieApiService;
    }


}