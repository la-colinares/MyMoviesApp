package com.app.mymoviesapp.Repositories;

import android.support.annotation.NonNull;
import android.util.Log;

import com.app.mymoviesapp.Constants.Constants;
import com.app.mymoviesapp.Interfaces.IOnGetGenreCallback;
import com.app.mymoviesapp.Interfaces.IOnGetMovieCallback;
import com.app.mymoviesapp.Interfaces.ITMDbAPI;
import com.app.mymoviesapp.Model.GenresResponse;
import com.app.mymoviesapp.Model.MoviesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Colinares on 5/19/2018.
 */

public class MoviesRepository {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";

    private static MoviesRepository repository;

    private ITMDbAPI api;

    private MoviesRepository(ITMDbAPI api) {
        this.api = api;
    }

    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesRepository(retrofit.create(ITMDbAPI.class));
        }

        return repository;
    }

    public void getMovies(int page, final IOnGetMovieCallback callback) {
        api.getPopularMovies(Constants.MOVIE_DB_API_KEY, LANGUAGE, page)
                .enqueue( new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getPage(),moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getGenres(final IOnGetGenreCallback callback) {
        api.getGenres(Constants.MOVIE_DB_API_KEY, LANGUAGE)
                .enqueue(new Callback<GenresResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GenresResponse> call, @NonNull Response<GenresResponse> response) {
                        if (response.isSuccessful()) {
                            GenresResponse genresResponse = response.body();
                            if (genresResponse != null && genresResponse.getGenres() != null) {
                                callback.onSuccess(genresResponse.getGenres());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenresResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }





}