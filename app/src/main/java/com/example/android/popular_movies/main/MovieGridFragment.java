package com.example.android.popular_movies.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popular_movies.R;
import com.example.android.popular_movies.models.Movie;
import com.example.android.popular_movies.movie_details.MovieDetailsActivity;
import com.example.android.popular_movies.utilities.Utilities;

import java.util.ArrayList;

/**
 * Created by SARVESH UPADHYAY on 17-03-2016.
 */
public class MovieGridFragment extends Fragment {

    private MovieGridAdapter movieGridAdapter;
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    public MovieGridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<Movie> popularMovies = new ArrayList<Movie>();
        //populateMoviesList(popularMovies);
        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.grid_view_movies);
        movieGridAdapter = new MovieGridAdapter(getContext(), popularMovies);
        gridview.setAdapter(movieGridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if( !new Utilities(getContext()).isConnectedToInternet() ) {
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                }  else {
                    Intent intent = new Intent(getActivity(), MovieDetailsActivity.class)
                            .putExtra("MoviePojo", movieGridAdapter.getPopularMovies().get(position));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_movie_grid, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if( !new Utilities(getContext()).isConnectedToInternet() ) {
            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        } else {
            if (id == R.id.action_sort_popularity) {
                getMovies("POPULAR");
            } else if (id == R.id.action_sort_ratings) {
                getMovies("TOP_RATED");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        getMovies();
    }

    private void getMovies() {
        if( !new Utilities(getContext()).isConnectedToInternet() ) {
            showAlert();
        } else {
            getMovies("NOW_PLAYING");
        }
    }

    private void getMovies(String sortBy) {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(movieGridAdapter);
        fetchMoviesTask.execute(sortBy);
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.internet_connection_request)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getMovies();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /**
                         * Android's design does not favor exiting an application by choice,
                         * but rather manages it by the OS.
                         * You can bring up the Home application by its corresponding Intent:
                         */
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).show();
    }


    /**
     * Following were added to dynamically load images
     * from drawables quite useful while designing the layout
     */

    /*
    private String resourceToUri(int resourceId) {
        final String ANDROID_RESOURCE = "android.resource://";
        final String FORWARD_SLASH = "/";
        return Uri.parse(ANDROID_RESOURCE + getContext().getPackageName()
                + FORWARD_SLASH + resourceId).toString();
    }

    private void populateMoviesList(ArrayList<Movie> popularMovies) {
        //Using Reflection to get all Images @ runtime
        Field[] ID_Fields = R.drawable.class.getFields();
        for( Field ID_Field:  ID_Fields) {
            if( ID_Field.getName().startsWith("sample_") ) {
                try {
                    popularMovies.add(new Movie( resourceToUri(ID_Field.getInt(null)) ) );
                } catch (IllegalAccessException e) {
                    Log.e(LOG_TAG, "Error", e);
                }
            }
        }
    }
    */

}
