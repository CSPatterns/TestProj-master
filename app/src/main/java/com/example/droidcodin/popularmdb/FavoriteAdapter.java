package com.example.droidcodin.popularmdb;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.droidcodin.popularmdb.data.MovieContract;

public class FavoriteAdapter extends CursorAdapter {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private static int sLoaderID;

    public static class ViewHolder {
        public final ImageView imageView;
       // public final TextView textView;

        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.list_item_movie_imageview);

        }
    }

    public FavoriteAdapter(Context context, Cursor c, int flags, int loaderID){
        super(context, c, flags);
        Log.d(LOG_TAG, "FlavAdapter");
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

//        int versionIndex = cursor.getColumnIndex(MovieContract.FlavorEntry.COLUMN_VERSION_NAME);
//        final String versionName = cursor.getString(versionIndex);
//        viewHolder.textView.setText(versionName);

        int imageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int image = cursor.getInt(imageIndex);
        Log.i(LOG_TAG, "Image reference extracted: " + image);

        viewHolder.imageView.setImageResource(image);

    }

}
