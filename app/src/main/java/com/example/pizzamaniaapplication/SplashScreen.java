package com.example.pizzamaniaapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUserLoggedIn()) {
                    // If already logged in → go to MainActivity
                    Intent i = new Intent(SplashScreen.this, LocationActivity.class);
                    startActivity(i);
                } else {
                    // If NOT logged in → go to LoginActivity
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                }

                // Close Splash
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("PizzaManiaPrefs", MODE_PRIVATE);
        return prefs.getBoolean("isLoggedIn", false);
    }
}
