package com.example.droidcodin.popularmdb;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by wuno on 11/6/15.
 */
public class MoviesResponse {


    private ArrayList<Movie> results;

    public ArrayList<Movie> getMovies() {
        return results;
    }

    public void setMovie(ArrayList<Movie> movies) {
        this.results = movies;
    }

    public static class Movie implements Parcelable {

        private String title;

        @SerializedName("poster_path")
        private String poster;

        @SerializedName("overview")
        private String description;

        @SerializedName("backdrop_path")
        private String backdrop;

        @SerializedName("release_date")
        private String releaseDate;

        @SerializedName("vote_average")
        private Double voteAverage;

        @SerializedName("id")
        private String movie_id;


        public Movie() {
        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPoster() {
            return "http://image.tmdb.org/t/p/w185" + poster;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getBackdrop() {
            return backdrop;
        }

        public void setBackdrop(String backdrop) {
            this.backdrop = backdrop;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public String getMovie_id() {
            return movie_id;
        }

        public void setMovie_id(String movie_id) {
            this.movie_id = movie_id;
        }

        protected Movie(Parcel in) {
            title = in.readString();
            poster = in.readString();
            description = in.readString();
            backdrop = in.readString();
            releaseDate = in.readString();
            voteAverage = in.readByte() == 0x00 ? null : in.readDouble();
            movie_id = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(poster);
            dest.writeString(description);
            dest.writeString(backdrop);
            dest.writeString(releaseDate);
            if (voteAverage == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeDouble(voteAverage);
            }
            dest.writeString(movie_id);
        }

        @SuppressWarnings("unused")
        public static final Creator<Movie> CREATOR = new Creator<Movie>() {
            @Override
            public Movie createFromParcel(Parcel in) {
                return new Movie(in);
            }

            @Override
            public Movie[] newArray(int size) {
                return new Movie[size];
            }
        };
    }
}
