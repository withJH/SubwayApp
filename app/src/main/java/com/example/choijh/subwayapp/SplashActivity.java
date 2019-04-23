package com.example.choijh.subwayapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SubscriptionManager;

public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, Subway_main.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}