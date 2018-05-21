package com.app.mymoviesapp.Interfaces;

import com.app.mymoviesapp.Model.Genre;

import java.util.List;

/**
 * Created by Colinares on 5/19/2018.
 */

public interface IOnGetGenreCallback {

    void onSuccess(List<Genre> genres);

    void onError();

}
