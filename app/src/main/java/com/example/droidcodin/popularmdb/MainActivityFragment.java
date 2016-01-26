package com.example.droidcodin.popularmdb;

/**
 * Created by Archie David on 23/12/15.
 */

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.droidcodin.popularmdb.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private GridView mGridView;
    private MovieAdapter mMovieAdapter;
    private ArrayList<MoviesResponse.Movie> movieList; // = new ArrayList<>();
    private RestClient restclient;
    private String apiKey = BuildConfig.PIMDB_API_KEY;
    private static final int CURSOR_LOADER_ID = 0;

    private List<ReviewList.Review> reviewlist;;


    // check network connectivity
    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }



    public MainActivityFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Cursor c =
                getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        new String[]{MovieContract.MovieEntry._ID},
                        null,
                        null,
                        null);
        if (c.getCount() == 0){
            updateMovie();
        }
        // initialize loader
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            movieList = new ArrayList<MoviesResponse.Movie>();

        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("movie");
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", (ArrayList)movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovie();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        // initialize our MovieAdapter
        mMovieAdapter = new MovieAdapter(getActivity(), null, 0, CURSOR_LOADER_ID);
        // initialize mGridView to the GridView in fragment_main.xml
        mGridView = (GridView) rootView.findViewById(R.id.movie_grid);
        // set mGridView adapter to our CursorAdapter
        mGridView.setAdapter(mMovieAdapter);

        // make each item clickable
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // increment the position to match Database Ids indexed starting at 1
                int uriId = position + 1;
                // append Id to uri
                Uri uri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI,
                        uriId);
                // create fragment
                MovieDetailFragment2 detailFragment = MovieDetailFragment2.newInstance(uriId, uri);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null).commit();
            }
        });


        return rootView;
    }

    // insert data into database
    public void updateFavoriteFLag() {

        //Utility.storeMovieList(getActivity(), movieList);
    }


    // Attach loader to our movies database query
    // run when loader is initialized
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        Log.v(LOG_TAG, "CreateLoader finished:" + id);
        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        super.onViewCreated(view, savedInstanceState);
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "Loader finished:" + loader.getId());
        mMovieAdapter.swapCursor(data);

    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        Log.v(LOG_TAG, "LoaderReset finished:" + loader.getId());
        mMovieAdapter.swapCursor(null);
    }


    private void updateMovie() {
        String preferredSortBy = Utility.getPreferredSortBy(getActivity());
        restclient = new RestClient();
        if (preferredSortBy.equals("popular")) {
            getData(restclient.getApiService(), apiKey, "popular");
        } else {
            getData(restclient.getApiService(), apiKey, "top_rated");
        }
    }


    //get datafrom server and fill the customer  adapter
    public void getData (MovieApiService get, String apikey, String orderingType){
        get.getMovie(orderingType, apikey, new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse movies, Response response) {
                movieList = movies.getMovies();

                if (movieList != null ) {
                    //bulkInsert arraylist of movies
                    Utility.storeMovieList(getActivity(), movieList);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Your Request have Problem", Toast.LENGTH_LONG).show();
            }
        });
    }


}

