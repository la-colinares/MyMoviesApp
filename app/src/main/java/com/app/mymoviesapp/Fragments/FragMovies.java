package com.app.mymoviesapp.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.app.mymoviesapp.Activities.MovieDetailsActivity;
import com.app.mymoviesapp.Adapter.MyMoviesAdapter;
import com.app.mymoviesapp.Interfaces.IOnGetGenresCallback;
import com.app.mymoviesapp.Interfaces.IOnGetMoviesCallback;
import com.app.mymoviesapp.Interfaces.OnMoviesClickCallback;
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
    private SwipeRefreshLayout mSwipeRefresh;
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

        mSwipeRefresh = mView.findViewById(R.id.refresh_movies);
        mSwipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.GRAY, Color.YELLOW, Color.MAGENTA, Color.CYAN);

        myMoviesList = mView.findViewById(R.id.movies_recycler_view);
        btnSort = mView.findViewById(R.id.btn_sort);
        btnSort.setText(getResources().getString(R.string.sort_popular));

        moviesRepository = MoviesRepository.getInstance();

        setupOnScrollListener();

        getGenres();

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGenres();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showSortMenu();
                showSortMenuV2();
            }
        });

        return mView;
    }

    private void showSortMenuV2() {

        final View viewPopup = getLayoutInflater().inflate(R.layout.custom_sort_by, null);

        final Button btnPopular = viewPopup.findViewById(R.id.btn_sort_popular);
        final Button btnTopRated = viewPopup.findViewById(R.id.btn_sort_top_rated);
        final Button btnUpcoming = viewPopup.findViewById(R.id.btn_sort_upcoming);

        final PopupWindow popupWindow = new PopupWindow(viewPopup, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setAnimationStyle(R.style.popup_window_animation_sort);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAtLocation(btnSort, Gravity.CENTER, 0, 0);

        btnPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSort.setText(getResources().getString(R.string.sort_popular));
                sortBy = MoviesRepository.POPULAR;
                getMovies(currentPage);
                popupWindow.dismiss();

            }
        });
        btnTopRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSort.setText(getResources().getString(R.string.sort_top_rated));
                sortBy = MoviesRepository.TOP_RATED;
                getMovies(currentPage);
                popupWindow.dismiss();
            }
        });
        btnUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSort.setText(getResources().getString(R.string.sort_upcoming));
                sortBy = MoviesRepository.UPCOMING;
                getMovies(currentPage);
                popupWindow.dismiss();
            }
        });

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

    private void getGenres() {
        moviesRepository.getGenres(new IOnGetGenresCallback() {
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
        moviesRepository.getMovies(page, sortBy, new IOnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                Log.e("MoviesRepository", "Current Page = " + page);
                if (mAdapter == null) {
                    mAdapter = new MyMoviesAdapter(movies, movieGenres, callback);
                    myMoviesList.setAdapter(mAdapter);
                } else {
                    if (page == 1) {
                        mAdapter.clearMovies();
                    }
                    mAdapter.appendMovies(movies);
                }
                mSwipeRefresh.setRefreshing(false);
                currentPage = page;
                isFetchingMovies = false;
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
            intent.putExtra(MovieDetailsActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };


    private void showError() {
        Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }


}
