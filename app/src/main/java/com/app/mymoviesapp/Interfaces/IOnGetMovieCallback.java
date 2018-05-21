package com.app.mymoviesapp.Interfaces;

import com.app.mymoviesapp.Model.Movie;

import java.util.List;

/**
 * Created by Colinares on 5/19/2018.
 */

public interface IOnGetMovieCallback {

    void onSuccess(int page,List<Movie> movies);

    void onError();

}
