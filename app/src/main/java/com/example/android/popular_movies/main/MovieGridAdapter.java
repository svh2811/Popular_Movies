package com.example.android.popular_movies.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.popular_movies.R;
import com.example.android.popular_movies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SARVESH UPADHYAY on 06-03-2016.
 */

public class MovieGridAdapter extends BaseAdapter {

    private final String LOG_TAG = MovieGridAdapter.class.getSimpleName().toString();
    private Context context;

    private ArrayList<Movie> popularMovies;

    public MovieGridAdapter(Context context, ArrayList<Movie> popularMovies) {
        this.context=context;
        this.popularMovies=popularMovies;
    }

    public void setPopularMovies(ArrayList<Movie> popularMovies) {
        this.popularMovies = popularMovies;
    }

    public ArrayList<Movie> getPopularMovies() {
        return popularMovies;
    }

    @Override
    public int getCount() {
        if( popularMovies!=null && popularMovies.size()> 0 ) {
            return popularMovies.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return popularMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            View v = LayoutInflater.from(context).inflate(R.layout.single_grid_item, null);
            imageView = (ImageView)v.findViewById(R.id.movie_image);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(context)
                    .load(popularMovies.get(position).getPosterPath())
                    .placeholder(R.mipmap.ic_placeholder)
                    .error(R.mipmap.ic_placeholder)
                    .resize(imageView.getMaxWidth(), imageView.getMaxHeight())
                    .onlyScaleDown() //since size of downloaded image is not known
                    .into(imageView);

        return imageView;
    }
}
