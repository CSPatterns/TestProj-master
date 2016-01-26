package com.example.droidcodin.popularmdb;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.droidcodin.popularmdb.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MovieDetailFragment2 extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private String apiKey = BuildConfig.PIMDB_API_KEY;

    List<ReviewList.Review> reviewlist;
    private ArrayAdapter<String> mReviewAdapter;
    private Cursor mDetailCursor;
    private View mRootView;
    private int mPosition;

    private String imageURL;
    private String movieID;

    private ImageView mImageview;
    private TextView mRating;
    private TextView mTitleTextView;
    private TextView mReleaseDate;
    private TextView mOverview;

    private TextView mUriText;
    private Uri mUri;
    private static final int CURSOR_LOADER_ID = 0;


    public static MovieDetailFragment2 newInstance(int position, Uri uri) {
        MovieDetailFragment2 fragment = new MovieDetailFragment2();
        Bundle args = new Bundle();
        fragment.mPosition = position;
        fragment.mUri = uri;
        args.putInt("id", position);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDetailFragment2() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.movie_fragment_detail, container, false);

        mImageview = (ImageView) rootView.findViewById(R.id.movie_detail_poster_imageview);
        mTitleTextView = (TextView) rootView.findViewById(R.id.detail_title_textview);
        mRating = (TextView) rootView.findViewById(R.id.movie_rating_textview);
        mReleaseDate = (TextView)rootView.findViewById(R.id.movie_releasedate_textview);
        mOverview = (TextView)rootView.findViewById(R.id.detail_plot_synopsis_textview);
        mUriText = (TextView) rootView.findViewById(R.id.uri);
        Bundle args = this.getArguments();
        getLoaderManager().initLoader(CURSOR_LOADER_ID, args, MovieDetailFragment2.this);


       // fetchMovieReviews();
        return rootView;

    }


    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        String selection = null;
        String [] selectionArgs = null;
        if (args != null){
            selection = MovieContract.MovieEntry._ID;
            selectionArgs = new String[]{String.valueOf(mPosition)};
        }
        return new CursorLoader(getActivity(),
                mUri,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mDetailCursor = data;
        mDetailCursor.moveToFirst();
        DatabaseUtils.dumpCursor(data);

        imageURL = mDetailCursor.getString(3);
        movieID = mDetailCursor.getString(1);

        mTitleTextView.setText(mDetailCursor.getString(2));

        Picasso.with(this.getActivity())
                .load(imageURL)
                .into(mImageview);
        //mImageview.setImageResource(mDetailCursor.getInt(3));
        mOverview.setText(mDetailCursor.getString(5));
        mReleaseDate.setText(mDetailCursor.getString(6));
        mRating.setText(mDetailCursor.getString(8));
        // set Uri to be displayed
        mUriText.setText(mUri.toString());
        fetchMovieReviews();
    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mDetailCursor = null;
    }


    private void fetchMovieReviews() {
        RestClient restclient = new RestClient();
        restclient.getApiService().getReview(movieID, apiKey, new Callback<ReviewList>() {

                    @Override
                    public void success(ReviewList reviewList, Response response) {
                        //List<ReviewList> reviewList =reviewList.getReviews();

                        reviewlist = reviewList.getReviews();

                        //get the name of each movie in the list and add it to movies_names list
                        List<String> listOfReviews = new ArrayList<>();
                        for (ReviewList.Review review : reviewlist) {
                            listOfReviews.add(review.getContent());
                            listOfReviews.add(review.getAuthor());
                        }

                        mReviewAdapter = new ArrayAdapter<String>(
                                getActivity(), // The current context (this activity)
                                R.layout.list_item_review, // The name of the layout ID.
                                R.id.list_item_review_textview, // The ID of the textview to populate.
                                listOfReviews);

                        ListView listView = (ListView) getView().findViewById(R.id.listview_review);
                        listView.setAdapter(mReviewAdapter);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), "Connection failed", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

}
