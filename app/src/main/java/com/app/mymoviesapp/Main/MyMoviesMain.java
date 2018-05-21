package com.app.mymoviesapp.Main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.app.mymoviesapp.Fragments.FragMovies;
import com.app.mymoviesapp.R;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class MyMoviesMain extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;

    private ResideMenu resideMenu;

    private ResideMenuItem itemMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_movies_main);

        mContext = this;

        setUpMenu();

        if( savedInstanceState == null ) {
            changeFragment(new FragMovies());
        }

    }

    private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(mContext);

        resideMenu.setBackground(R.color.colorPrimaryDark);
        resideMenu.attachToActivity(this);
        //resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemMovies = new ResideMenuItem(mContext, R.drawable.ic_movie_white,"Movies");

        itemMovies.setOnClickListener(this);

        resideMenu.addMenuItem(itemMovies, ResideMenu.DIRECTION_LEFT);

        findViewById(R.id.left_menu_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void onClick(View view) {
        if (view == itemMovies){
            changeFragment(new FragMovies());
        }

        resideMenu.closeMenu();
    }
}
