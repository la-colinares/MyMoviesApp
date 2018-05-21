package com.app.mymoviesapp.Interfaces;

import com.app.mymoviesapp.Model.Trailer;

import java.util.List;

/**
 * Created by Colinares on 5/21/2018.
 */

public interface IOnGetTrailersCallback {
    void onSuccess(List<Trailer> trailers);

    void onError();

}
