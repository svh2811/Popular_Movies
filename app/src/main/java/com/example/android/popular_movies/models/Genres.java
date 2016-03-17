package com.example.android.popular_movies.models;

/**
 * Created by SARVESH UPADHYAY on 13-03-2016.
 *
 * Ideally this should be a stored somewhere (common Db_table maybe)
 * and should be periodically populated based on json response of
 * http://api.themoviedb.org/3/genre/movie/list?api_key=XXXXXXXXXXX
 */

public enum Genres {

    Action(28), Adventure(12), Animation(16),
    Comedy(35), Crime(80), Documentary(99),
    Drama(18), Family(10751), Fantasy(14),
    Foreign(10769), History(36), Horror(27),
    Music(10402), Mystery(9648), Romance(10749),
    Science_Fiction(878), TV_Movie(10770),
    Thriller(53), War(10752), Western(37);

    private final int id;

    Genres(int id) {
        this.id=id;
    }

    private int getId() {
        return id;
    }

    public static String getGenreNameFromId(int id) {
        String genreName = null;

        for (Genres g : Genres.values()) {
            if( g.getId() == id ){
                genreName = g.name().replace('_', ' ');
                break;
            }
        }

        return genreName;
    }
}
