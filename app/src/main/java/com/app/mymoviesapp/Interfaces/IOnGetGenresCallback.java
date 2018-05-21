package com.app.mymoviesapp.Interfaces;

import com.app.mymoviesapp.Model.Genre;

import java.util.List;

/**
 * Created by Colinares on 5/19/2018.
 */

public interface IOnGetGenresCallback {

    void onSuccess(List<Genre> genres);

    void onError();

}
