package com.example.android.popular_movies.main;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popular_movies.BuildConfig;
import com.example.android.popular_movies.models.Genres;
import com.example.android.popular_movies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SARVESH UPADHYAY on 10-03-2016.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private MovieGridAdapter movieGridAdapter;
    private final String API_KEY =  BuildConfig.TMDB_API_KEY;

    public FetchMoviesTask(MovieGridAdapter movieGridAdapter) {
        this.movieGridAdapter=movieGridAdapter;
    }

    @Override
    protected void onPreExecute() {

        /*// TODO: 10-03-2016
         * Implement loading spinner while doInBackground is fetching movies
         * Question: is it really neccesary sice AsyncTasks are not supposed
         * to be longer than 3-4 sec
         * */

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> popularMovies) {
        super.onPostExecute(popularMovies);
        movieGridAdapter.setPopularMovies(popularMovies);
        movieGridAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String popularMoviesJsonStr = null;

        String listOf = (params.length==1) ? params[0] : "";

        if( listOf.equalsIgnoreCase("POPULAR") ) {
            listOf = "popular";
        } else if( listOf.equalsIgnoreCase("TOP_RATED") ) {
            listOf = "top_rated";
            /**
             * This could have been used as well
             * http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc
             */
        } else {
            listOf = "now_playing";
        }

        ArrayList<Movie> popularMovies = null;

        final String BASE_URL = BuildConfig.TMDB_BASE_URL;

        try {

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(listOf)
                    .appendQueryParameter("api_key", API_KEY)
                    .build();

            URL url = new URL( builtUri.toString() );

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                popularMoviesJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                popularMoviesJsonStr = null;
            }
            popularMoviesJsonStr = buffer.toString();

            try {
                popularMovies = getMoviesFromJsonString(popularMoviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error", e);
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            popularMoviesJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return popularMovies;
    }

    private ArrayList<Movie> getMoviesFromJsonString(String jsonStr) throws JSONException{
        ArrayList<Movie> popularMovies = new ArrayList<Movie>();

        String TMDb_RESULT = "results";
        String TMDb_ID = "id";
        String TMDb_TITLE = "title";
        String TMDb_OVERVIEW = "overview";
        String TMDb_BACKDROP_PATH = "backdrop_path";
        String TMDb_POSTER_PATH = "poster_path";
        String TMDb_POPULARITY = "popularity";
        String TMDb_VOTE_COUNT = "vote_count";
        String TMDb_VOTE_AVERAGE = "vote_average";
        String TMDb_RELEASE_DATE = "release_date";
        String TMDb_GENRE_IDS = "genre_ids";

        JSONObject moviesJson = new JSONObject(jsonStr);
        JSONArray movieDetailsArray = moviesJson.getJSONArray(TMDb_RESULT);

        for(int i=0; i<movieDetailsArray.length(); i++) {
            JSONObject movieJSONObj = movieDetailsArray.getJSONObject(i);
            Movie movie = new Movie(
                    movieJSONObj.getInt(TMDb_ID),
                    movieJSONObj.getString(TMDb_TITLE),
                    movieJSONObj.getString(TMDb_OVERVIEW),
                    generateUriStringFromFilePath( movieJSONObj.getString(TMDb_BACKDROP_PATH), 'B' ),
                    generateUriStringFromFilePath( movieJSONObj.getString(TMDb_POSTER_PATH), 'P' ),
                    movieJSONObj.getDouble(TMDb_POPULARITY),
                    movieJSONObj.getInt(TMDb_VOTE_COUNT),
                    round(movieJSONObj.getDouble(TMDb_VOTE_AVERAGE), 1),
                    movieJSONObj.getString(TMDb_RELEASE_DATE).substring(0, 4),
                    getGenreNames(movieJSONObj.getJSONArray(TMDb_GENRE_IDS))
                    );

            popularMovies.add(movie);
        }

        return popularMovies;
    }

    private String generateUriStringFromFilePath(String path, char imageType) {

        //E.g.: http://image.tmdb.org/t/p/w500/811DjJTon9gD6hZ8nCjSitaIXFQ.jpg
        final String BASE_URL = BuildConfig.IMAGE_BASE_URL;

        //Aspect Ratio --> 2:3
        final String POSTER_SIZE = BuildConfig.POSTER_SIZE;

        //Aspect Ratio --> 16.9
        final String BACKDROP_SIZE = BuildConfig.BACKDROP_SIZE;

        //Added Since Response JSON returns /...jpg and '/' is URLEncoded
        //which messes up the entire link
        path = path.substring(1);

        if( imageType=='P' ) {
            return Uri.parse(BASE_URL).buildUpon()
                    .appendPath(POSTER_SIZE)
                    .appendPath(path)
                    .appendQueryParameter("api_key", API_KEY)
                    .build().toString();
        } else {
            return Uri.parse(BASE_URL).buildUpon()
                    .appendPath(BACKDROP_SIZE)
                    .appendPath(path)
                    .appendQueryParameter("api_key", API_KEY)
                    .build().toString();
        }
    }

    private List<String> getGenreNames(JSONArray genreIds) throws JSONException {
        List<String> genreNames = new ArrayList<String>();
        for(int i=0; i<genreIds.length(); i++ ){
            genreNames.add(Genres.getGenreNameFromId(genreIds.getInt(i)));
        }

        return genreNames;
    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

}
