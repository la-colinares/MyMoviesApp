package com.app.mymoviesapp.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.mymoviesapp.Main.MyMoviesMain;
import com.app.mymoviesapp.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Thread loading = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                    startActivity(new Intent(SplashScreen.this, MyMoviesMain.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        loading.start();

    }
}
