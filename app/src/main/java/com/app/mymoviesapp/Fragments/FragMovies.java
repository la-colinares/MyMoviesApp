package com.app.mymoviesapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
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
    private Button btnSort;
    private MyMoviesAdapter mAdapter;

    private MoviesRepository moviesRepository;

    private List<Genre> movieGenres;

    private boolean isFetchingMovies;
    private int currentPage = 1;

    private String sortBy = MoviesRepository.POPULAR;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_movies, container, false);

        myMoviesList = mView.findViewById(R.id.movies_recycler_view);
        btnSort = mView.findViewById(R.id.btn_sort);
        btnSort.setText(getResources().getString(R.string.sort_popular));

        moviesRepository = MoviesRepository.getInstance();

        setupOnScrollListener();

        getGenres();

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSortMenu();
            }
        });

        return mView;
    }

    private void showSortMenu() {
        PopupMenu sortMenu = new PopupMenu(getActivity(), mView.findViewById(R.id.btn_sort));
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                currentPage = 1;
                switch (item.getItemId()) {
                    case R.id.popular:
                        btnSort.setText(getResources().getString(R.string.sort_popular));
                        sortBy = MoviesRepository.POPULAR;
                        getMovies(currentPage);
                        return true;
                    case R.id.top_rated:
                        btnSort.setText(getResources().getString(R.string.sort_top_rated));
                        sortBy = MoviesRepository.TOP_RATED;
                        getMovies(currentPage);
                        return true;
                    case R.id.upcoming:
                        btnSort.setText(getResources().getString(R.string.sort_upcoming));
                        sortBy = MoviesRepository.UPCOMING;
                        getMovies(currentPage);
                        return true;
                    default:
                        return false;
                }
            }
        });

        sortMenu.inflate(R.menu.sort_menu);
        sortMenu.show();
    }

    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        myMoviesList.setLayoutManager(manager);
        myMoviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
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
        moviesRepository.getMovies(page, sortBy, new IOnGetMovieCallback() {
            @Override
            public void onSuccess(int page,  List<Movie> movies) {
                Log.e("MoviesRepository", "Current Page = " + page);
                if (mAdapter == null) {
                    mAdapter = new MyMoviesAdapter(movies, movieGenres);
                    myMoviesList.setAdapter(mAdapter);
                } else {
                    if (page == 1){
                        mAdapter.clearMovies();
                    }
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
