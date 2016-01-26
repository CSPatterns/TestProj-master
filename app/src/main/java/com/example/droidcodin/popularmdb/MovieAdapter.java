package com.example.droidcodin.popularmdb;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.droidcodin.popularmdb.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by Archie David on 23/12/15.
 */


public class MovieAdapter extends CursorAdapter {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private static int sLoaderID;

    public static class ViewHolder {
        public final ImageView imageView;


        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.list_item_movie_imageview);
        }
    }

    public MovieAdapter(Context context, Cursor c, int flags, int loaderID){
        super(context, c, flags);
        Log.d(LOG_TAG, "MovieAdapterLog");
        mContext = context;
        sLoaderID = loaderID;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        int layoutId = R.layout.list_item_movie;

        Log.d(LOG_TAG, "In new View");

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Log.d(LOG_TAG, "In bind View");


        int imageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE);
        //byte[] image = cursor.getBlob(imageIndex);
        String image = cursor.getString(imageIndex);
        if (image != null) {
            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w185/" + image)
                    .resize(580,800)
                    .centerCrop()
                    .into(viewHolder.imageView);
        }

    }
}



//public class MovieAdapter extends ArrayAdapter<Movie> {
//
//    //String url = "http://image.tmdb.org/t/p/w185";
//    private Context context;
//    private ArrayList<Movie> movieList;
//    public MovieAdapter(Context context, int resource, ArrayList<Movie> objects) {
//        super(context, resource, objects);
//        this.context = context;
//        this.movieList = objects;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//
//        ImageView imageView;
//
//        Movie movie = movieList.get(position);
//
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
//            imageView = new ImageView(context);
//            imageView.setLayoutParams(new GridView.LayoutParams(580, 810));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(1, 8, 8, 8);
//        } else {
//            imageView = (ImageView) convertView;
//        }
//
//
//
//        Picasso.with(context).load(movie.getPoster()).into(imageView);
//        return imageView;
//
//    }
//}
