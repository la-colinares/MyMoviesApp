package com.app.mymoviesapp.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.mymoviesapp.Main.MyMoviesMain;
import com.app.mymoviesapp.R;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView mImage;

    private int progressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mImage = findViewById(R.id.splash_image);
        progressBar = findViewById(R.id.progressBar);

        Animation goRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);

        mImage.startAnimation(goRotate);

        final Thread loading = new Thread() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 20;
                    try {
                        Thread.sleep(500);
                        progressBar.setProgress(progressStatus);
                        if (progressStatus == 100) {
                            startActivity(new Intent(SplashScreen.this, MyMoviesMain.class));
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        loading.start();

    }
}
