package com.j7arsen.simple.gallery.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.j7arsen.simple.gallery.activities.MainActivity;
import com.j7arsen.simple.gallery.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by arsen on 10.01.2016.
 */
public class SplashScreenActivity extends Activity {

    private static final int SPLASH_TIME_OUT = 2000;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_layout);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this,
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        if (mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
