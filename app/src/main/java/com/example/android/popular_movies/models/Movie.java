package com.example.android.popular_movies.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SARVESH UPADHYAY on 12-03-2016.
 */
public class Movie implements Serializable {

    private Integer id;
    private String title;
    //private String originalTitle;
    //private String originalLanguage;
    private String overView;
    private String backdropPath;
    private String posterPath;
    private Double popularity;
    private Integer voteCount;
    private Double voteAverage;
    //private Boolean video;
    //private Boolean adult;
    private String releaseDate;
    private List<String> genres;

    public Movie(String posterPath) {
        this.posterPath = posterPath;
    }

    public Movie(Integer id, String title, String overView, String backdropPath, String posterPath,
                 Double popularity, Integer voteCount, Double voteAverage, String releaseDate, List<String> genres) {
        this.id = id;
        this.title = title;
        this.overView = overView;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.genres = genres;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverView() {
        return overView;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<String> getGenres() {
        return genres;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overView='" + overView + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", popularity=" + popularity +
                ", voteCount=" + voteCount +
                ", voteAverage=" + voteAverage +
                ", releaseDate='" + releaseDate + '\'' +
                ", genres=" + genres +
                '}';
    }


}
