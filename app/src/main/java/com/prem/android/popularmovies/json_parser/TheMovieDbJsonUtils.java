package com.prem.android.popularmovies.json_parser;

import com.prem.android.popularmovies.models.Movies;
import com.prem.android.popularmovies.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Prem on 18-07-2017.
 * This is a util class for parsing the JSON data returned by API and returned as Movie Object.
 */
public class TheMovieDbJsonUtils {

    public static ArrayList<Movies> getMovieListFromJson(String responseFromAPI) throws JSONException {

        ArrayList<Movies> movieList = new ArrayList<>();


        if (responseFromAPI != null) {

            JSONObject reader = new JSONObject(responseFromAPI);
            JSONArray arrayOfMoviesObject = reader.getJSONArray(Constants.RESULTS);


            //Parsing the objects of reader Array

            for (int i = 0; i < arrayOfMoviesObject.length(); i++) {

                Movies currentMovie = new Movies();

                JSONObject objectOfReaderArray = arrayOfMoviesObject.getJSONObject(i);

                currentMovie.setTitle(objectOfReaderArray.getString(Constants.TITLE_OF_MOVIE));
                currentMovie.setPoster(objectOfReaderArray.getString(Constants.THUMBNAIL_OF_MOVIE));
                currentMovie.setReleaseDate(objectOfReaderArray.getString(Constants.RELEASE_DATE));
                currentMovie.setUserRating(objectOfReaderArray.getString(Constants.RATING_OF_MOVIE));
                currentMovie.setOverview(objectOfReaderArray.getString(Constants.OVERVIEW_OF_MOVIE));
                currentMovie.setIdMovie(objectOfReaderArray.getInt(Constants.ID_OF_MOVIE));

                movieList.add(currentMovie);
            }
        }
        return movieList;
    }
}
