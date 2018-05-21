package com.app.mymoviesapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.mymoviesapp.Adapter.MyMoviesAdapter;
import com.app.mymoviesapp.Interfaces.IOnGetGenreCallback;
import com.app.mymoviesapp.Interfaces.IOnGetMovieCallback;
import com.app.mymoviesapp.Model.Genre;
import com.app.mymoviesapp.Model.Movie;
import com.app.mymoviesapp.R;
import com.app.mymoviesapp.Repositories.MoviesRepository;

import java.util.List;

/**
 * Created by Colinares on 5/19/2018.
 */

public class FragMovies extends Fragment {

    private View mView;
    private RecyclerView myMoviesList;
    private MyMoviesAdapter mAdapter;

    private MoviesRepository moviesRepository;

    private List<Genre> movieGenres;

    private boolean isFetchingMovies;
    private int currentPage = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_movies, container, false);

        myMoviesList = mView.findViewById(R.id.movies_recycler_view);

        moviesRepository = MoviesRepository.getInstance();

        setupOnScrollListener();

        getGenres();

        return mView;
    }

    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        myMoviesList.setLayoutManager(manager);
        myMoviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }

    /*private void getGenres() {
        moviesRepository.getGenres(new IOnGetGenreCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                displayMovies(genres);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }*/
    private void getGenres() {
        moviesRepository.getGenres(new IOnGetGenreCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                movieGenres = genres;
                getMovies(currentPage);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getMovies(int page) {
        isFetchingMovies = true;
        moviesRepository.getMovies(page, new IOnGetMovieCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                Log.e("MoviesRepository", "Current Page = " + page);
                if (mAdapter == null) {
                    mAdapter = new MyMoviesAdapter(movies, movieGenres);
                    myMoviesList.setAdapter(mAdapter);
                } else {
                    mAdapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    /*private void displayMovies(final List<Genre> genres) {
        moviesRepository.getMovies(new IOnGetMovieCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {

                mAdapter = new MyMoviesAdapter(movies, genres);
                myMoviesList.setAdapter(mAdapter);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }*/

    private void showError() {
        Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }


}
