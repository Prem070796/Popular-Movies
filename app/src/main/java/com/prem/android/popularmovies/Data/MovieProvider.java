package com.prem.android.popularmovies.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Prem on 08-09-2017.
 */

public class MovieProvider extends ContentProvider {

    //Create a private member variable to hold an instance of Movie Database  which is used to access
    // the database itself and instantiate it in onCreate method
    private MovieDbHelper movieDbHelper;

    // Use an int for each URI we will run, this represents the different queries
    private static final int MOVIE = 200;
    private static final int MOVIE_ID = 201;

    //URIMatcher that will take in a URI and match it to the appropriate integer identifier we just defined
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * Builds a UriMatcher that is used to determine witch database request is being made.
     */
    private static UriMatcher buildUriMatcher() {
        String contentAuthority = MovieContract.CONTENT_AUTHORITY;

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above)
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(contentAuthority, MovieContract.TABLE_MOVIE_NAME, MOVIE);
        matcher.addURI(contentAuthority, MovieContract.TABLE_MOVIE_NAME + "/#", MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s,
                        @Nullable String[] strings1, @Nullable String s1) {

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        Cursor retCursor;

        switch(sUriMatcher.match(uri)){
            case MOVIE:
                retCursor = db.query(
                        MovieContract.TABLE_MOVIE_NAME,
                        strings,
                        s,
                        strings1,
                        null,
                        null,
                        s1
                );
                break;

            case MOVIE_ID:
                long _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        MovieContract.TABLE_MOVIE_NAME,
                        strings,
                        MovieContract.MovieEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        s1
                );
                break;

            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        // Set the notification URI for the cursor to the one passed into the function. This
        // causes the cursor to register a content observer to watch for changes that happen to
        // this URI and any of it's descendants. By descendants, we mean any URI that begins
        // with this path.
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int rows; // Number of rows effected

        switch(sUriMatcher.match(uri)){

            case MOVIE:
                rows = db.delete(MovieContract.TABLE_MOVIE_NAME, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because null could delete all rows:
        if(s == null || rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int rows;

        switch(sUriMatcher.match(uri)){
            case MOVIE:
                rows = db.update(MovieContract.TABLE_MOVIE_NAME, contentValues, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }
}
