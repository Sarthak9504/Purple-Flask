package com.example.dbms_lab_inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {
    Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                long loggedIn = sh.getLong("isLogged", -1);
                if (loggedIn == -1) {
                    myIntent = new Intent(SplashScreen.this, Navigation.class);
                } else {
                    myIntent = new Intent(SplashScreen.this, MainActivity.class);
                    myIntent.putExtra("newLogin", false);
                }
                SplashScreen.this.startActivity(myIntent);
                finish();
            }
        },1000);
    }
}