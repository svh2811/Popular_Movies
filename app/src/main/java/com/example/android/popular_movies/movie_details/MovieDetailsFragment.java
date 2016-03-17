package com.example.android.popular_movies.movie_details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popular_movies.R;
import com.example.android.popular_movies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailsFragment extends Fragment {

    private final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
    private View rootView;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        Intent intent = getActivity().getIntent();
        Movie movie = null;

        if( intent!=null && intent.hasExtra("MoviePojo") ) {
            movie = (Movie) intent.getSerializableExtra("MoviePojo");
        } else {
            Log.e(LOG_TAG, "No intent info");
        }

        populateMovieViews(movie);
        return rootView;
    }

    private void populateMovieViews(Movie movie) {

        ImageView backDropImage = (ImageView) rootView.findViewById(R.id.movie_backdrop);

        Picasso.with(getActivity())
                .load(movie.getBackdropPath())
                .priority(Picasso.Priority.HIGH)
                .placeholder(R.mipmap.ic_placeholder)
                .error(R.mipmap.ic_placeholder)
                .resize(backDropImage.getMaxWidth(), backDropImage.getMaxHeight())
                .onlyScaleDown() //since size of downloaded image is not known
                .into(backDropImage);

        ImageView posterImage = (ImageView) rootView.findViewById(R.id.movie_poster);

        Picasso.with(getContext())
                .load(movie.getPosterPath())
                .priority(Picasso.Priority.NORMAL)
                .placeholder(R.mipmap.ic_placeholder)
                .error(R.mipmap.ic_placeholder)
                .resize(posterImage.getMaxWidth(), posterImage.getMaxHeight())
                .onlyScaleDown() //since size of downloaded image is not known
                .into(posterImage);

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(movie.getTitle());
        } catch (NullPointerException e) {}

        ((TextView) rootView.findViewById(R.id.movie_title) ).setText(movie.getTitle());
        ((TextView) rootView.findViewById(R.id.movie_ratings) ).setText(movie.getVoteAverage().toString());
        ((TextView) rootView.findViewById(R.id.movie_vote_count) ).setText(movie.getVoteCount().toString() + " votes");
        ((TextView) rootView.findViewById(R.id.movie_release_date) ).setText(movie.getReleaseDate());

        StringBuffer genres = new StringBuffer("");
        List<String> genresList = movie.getGenres();
        for(int i=0; i<genresList.size(); i++) {
            if( (i+1) == genresList.size() ) {
                genres.append(genresList.get(i));
            } else {
                genres.append(genresList.get(i)).append('\n');
            }
        }

        ((TextView) rootView.findViewById(R.id.movie_genres)).setText(genres);
        ((TextView) rootView.findViewById(R.id.movie_summary)).setText(movie.getOverView());
    }
}