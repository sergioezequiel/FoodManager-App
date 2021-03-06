package com.foodmanager.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;

import com.foodmanager.R;
import com.foodmanager.models.SingletonDatabaseManager;

public class SplashScreenActivity extends AppCompatActivity {
    /* For Splash Screen Variable Gives Time */
    private static int progressLimit = 111;
    private int progressStatus = 0;
    private ProgressBar progressBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.slpash_screen_progressBar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < progressLimit) {
                    progressStatus++;
                    SystemClock.sleep(11);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sharedPrefs = getSharedPreferences(LoginActivity.FOODMAN_SHARED_PREFS, Context.MODE_PRIVATE);
                        Intent activity;

                        if(sharedPrefs.getBoolean(LoginActivity.IS_LOGGED_IN, false)) {
                            SingletonDatabaseManager.getInstance(getApplicationContext()).setApikey(sharedPrefs.getString(LoginActivity.APIKEY, ""));
                            activity = new Intent(SplashScreenActivity.this, MainActivity.class);
                        }
                        else {
                            activity = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        }

                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        startActivity(activity);
                        finish();
                    }
                });
            }
        }).start();

    }
}