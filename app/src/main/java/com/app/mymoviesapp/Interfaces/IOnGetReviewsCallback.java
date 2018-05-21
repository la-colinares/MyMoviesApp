package com.app.mymoviesapp.Interfaces;

import com.app.mymoviesapp.Model.Review;

import java.util.List;

/**
 * Created by Colinares on 5/21/2018.
 */

public interface IOnGetReviewsCallback {
    void onSuccess(List<Review> reviews);

    void onError();
}
