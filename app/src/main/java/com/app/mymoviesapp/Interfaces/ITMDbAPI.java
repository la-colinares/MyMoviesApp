package com.app.mymoviesapp.Interfaces;

import com.app.mymoviesapp.Model.GenresResponse;
import com.app.mymoviesapp.Model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Colinares on 5/19/2018.
 */

public interface ITMDbAPI {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("genre/movie/list")
    Call<GenresResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

}
