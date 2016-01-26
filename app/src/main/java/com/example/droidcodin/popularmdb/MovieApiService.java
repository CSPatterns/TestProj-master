package com.example.droidcodin.popularmdb;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Archie David on 21/12/15.
 */
public interface MovieApiService {
//    @GET("/discover/movie")
//    public void getData(Callback<MoviesResponse> response);
    @GET("/3/movie/{movie}")
    public void getMovie(@Path("movie") String typeMovie, @Query("api_key") String keyApi, Callback<MoviesResponse> response);

    @GET("/3/movie/{idMovie}/reviews")
    public  void getReview(@Path("idMovie") String idMovie,@Query("api_key") String keyApi, Callback<ReviewList> response );

    @GET("/3/movie/{idMovie}/videos")
    public  void getVideo(@Path("idMovie") String idMovie, @Query("api_key") String keyApi, Callback<VideoList> response);
}
