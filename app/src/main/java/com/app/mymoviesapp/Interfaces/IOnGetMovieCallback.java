package com.app.mymoviesapp.Interfaces;

import com.app.mymoviesapp.Model.Movie;

/**
 * Created by Colinares on 5/21/2018.
 */

public interface IOnGetMovieCallback {
    void onSuccess(Movie movie);

    void onError();
}
