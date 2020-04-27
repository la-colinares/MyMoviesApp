package com.app.mymoviesapp.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mymoviesapp.Constants.Constants;
import com.app.mymoviesapp.Interfaces.OnMoviesClickCallback;
import com.app.mymoviesapp.Model.Genre;
import com.app.mymoviesapp.Model.Movie;
import com.app.mymoviesapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colinares on 5/19/2018.
 */

public class MyMoviesAdapter extends RecyclerView.Adapter<MyMoviesAdapter.MyMovieViewHolder>{

    private List<Genre> allGenres;
    private List<Movie> movies;
    private OnMoviesClickCallback callback;

    public MyMoviesAdapter(List<Movie> movies, List<Genre> allGenres, OnMoviesClickCallback callback) {
        this.movies = movies;
        this.allGenres = allGenres;
        this.callback = callback;
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    public void clearMovies() {
        movies.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_movies_layout, parent, false);
        return new MyMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyMovieViewHolder holder, int position) {
        // TODO: Populate adapter with movies
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MyMovieViewHolder extends RecyclerView.ViewHolder {

        TextView releaseDate;
        TextView title;
        TextView rating;
        TextView genres;
        ImageView poster;
        Movie movie;

        public MyMovieViewHolder(View itemView) {
            super(itemView);

            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_rating);
            genres = itemView.findViewById(R.id.item_movie_genre);
            poster = itemView.findViewById(R.id.item_movie_poster);
            itemView.setOnClickListener(view -> callback.onClick(movie));
        }

        public void bind(Movie movie) {
            this.movie = movie;
            releaseDate.setText(movie.getReleaseDate().split("-")[0]);
            title.setText(movie.getTitle());
            rating.setText(String.valueOf(movie.getRating()));
            genres.setText(getGenres(movie.getGenreIds()));

            Glide.with(itemView)
                    .load(Constants.IMAGE_BASE_URL + movie.getPosterPath())
                    .into(poster);
        }

        private String getGenres(List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (Genre genre : allGenres) {
                    if (genre.getId() == genreId) {
                        movieGenres.add(genre.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", movieGenres);
        }
    }
}
